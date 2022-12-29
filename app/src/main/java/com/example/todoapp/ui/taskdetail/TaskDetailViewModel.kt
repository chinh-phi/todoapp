package com.example.todoapp.ui.taskdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.base.BaseViewModel
import com.example.todoapp.data.database.Task
import com.example.todoapp.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val repository: TaskRepository
) : BaseViewModel() {

    val updateStatus = MutableLiveData<Boolean>(false)
    val deleteStatus = MutableLiveData<Boolean>(false)

    val taskInfo = MutableLiveData<Task>()
    var taskTitle: String? = null
    var taskDescription: String? = null
    var taskTime: Long? = null

    fun getTaskInfo(taskId: Int) {
        viewModelScope.launch {
            repository.getTaskById(taskId).collect() {
                taskInfo.postValue(it)
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            updateStatus.postValue(repository.updateTask(task) == 1)
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            deleteStatus.postValue(repository.deleteTask(taskId) == 1)
        }
    }

}