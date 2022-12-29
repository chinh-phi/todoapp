package com.example.todoapp.ui.categorylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.base.BaseViewModel
import com.example.todoapp.data.CategoryType
import com.example.todoapp.data.database.Task
import com.example.todoapp.data.repository.TaskRepository
import com.example.todoapp.model.TaskType
import com.example.todoapp.utils.TimeUtil
import com.example.todoapp.utils.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val repository: TaskRepository
) : BaseViewModel() {

    var currentTaskId = MutableLiveData<Long>(null)
    var taskIdInserted = MutableLiveData<Long>(null)

    val searchResult = MutableLiveData<List<Task>>(emptyList())

    private val categoryAll = MutableLiveData<List<Task>>()
    private val categoryToday = MutableLiveData<List<Task>>()
    private val categoryFuture = MutableLiveData<List<Task>>()
    private val categoryCompleted = MutableLiveData<List<Task>>()

    init {
        getTotalNumberOfCategoryAll()
        getTotalNumberOfCategoryToday(TimeUtil.getCurrentStartTimeOfDay(), TimeUtil.getCurrentEndTimeOfDay())
        getTotalNumberOfCategoryFuture(TimeUtil.getCurrentTime())
        getTotalNumberOfCategoryCompleted(TimeUtil.getCurrentTime())
    }

    val state: LiveData<CategoryState> = combine(
        CategoryState(),
        categoryAll,
        categoryToday,
        categoryFuture,
        categoryCompleted
    ) { currentValue, categoryAll, categoryToday, categoryFuture, categoryCompleted ->
        mapToState(
            currentValue,
            categoryAll,
            categoryToday,
            categoryFuture,
            categoryCompleted
        )
    }

    private fun mapToState(
        currentValue: CategoryState,
        categoryAll: List<Task>,
        categoryToday: List<Task>,
        categoryFuture: List<Task>,
        categoryCompleted: List<Task>
    ): CategoryState {

        val listCategory = listOf(
            TaskType(
                id = CategoryType.TODAY.categoryId,
                title = CategoryType.TODAY.categoryName,
                day = TimeUtil.getCurrentDayOfMonth(),
                totalTask = categoryToday.size
            ),
            TaskType(
                id = CategoryType.FUTURE.categoryId,
                title = CategoryType.FUTURE.categoryName,
                totalTask = categoryFuture.size,
                icon = CategoryType.FUTURE.icon
            ),
            TaskType(
                id = CategoryType.ALL.categoryId,
                title = CategoryType.ALL.categoryName,
                totalTask = categoryAll.size,
                icon = CategoryType.ALL.icon
            ),
            TaskType(
                id = CategoryType.COMPLETED.categoryId,
                title = CategoryType.COMPLETED.categoryName,
                totalTask = categoryCompleted.size,
                icon = CategoryType.COMPLETED.icon
            )
        )

        return currentValue.copy(
            categoryList = listCategory,
            listTaskToday = categoryToday,
            listTaskFuture = categoryFuture,
            listTaskAll = categoryAll,
            listTaskCompleted = categoryCompleted
        )
    }


    private fun getTotalNumberOfCategoryAll() {
        viewModelScope.launch {
            repository.getTotalNumberOfCategoryAll().collect {
                categoryAll.postValue(it)
            }
        }
    }

    private fun getTotalNumberOfCategoryToday(startTime: Long, endTime: Long) {
        viewModelScope.launch {
            repository.getTotalNumberOfCategoryToday(startTime, endTime).collect {
                categoryToday.postValue(it)
            }
        }
    }

    private fun getTotalNumberOfCategoryFuture(currentTime: Long) {
        viewModelScope.launch {
            repository.getTotalNumberOfCategoryFuture(currentTime).collect {
                categoryFuture.postValue(it)
            }
        }
    }

    private fun getTotalNumberOfCategoryCompleted(currentTime: Long) {
        viewModelScope.launch {
            repository.getTotalNumberOfCategoryCompleted(currentTime).collect {
                categoryCompleted.postValue(it)
            }
        }
    }

    fun insert(task: Task) {
        viewModelScope.launch {
            taskIdInserted.value = repository.insertTask(task)
        }
    }

    fun deleteAllTask() = viewModelScope.launch {
        repository.deleteAllTask()
    }

    fun doSearch(key: String) {
        viewModelScope.launch {
            searchResult.postValue(repository.searchTask("%$key%"))
        }
    }
}

data class CategoryState(
    val categoryList: List<TaskType> = emptyList(),
    val listTaskToday: List<Task>? = null,
    val listTaskFuture: List<Task>? = null,
    val listTaskAll: List<Task>? = null,
    val listTaskCompleted: List<Task>? = null
)