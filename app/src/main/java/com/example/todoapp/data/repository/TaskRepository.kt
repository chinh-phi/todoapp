package com.example.todoapp.data.repository

import com.example.todoapp.data.database.Task
import com.example.todoapp.data.database.TaskDao
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {

    fun getTaskById(taskId: Int): Flow<Task> = taskDao.getTaskById(taskId)

    fun getTotalNumberOfCategoryAll(): Flow<List<Task>> = taskDao.getTotalNumberOfCategoryAll()

    fun getTotalNumberOfCategoryToday(
        startTime: Long,
        endTime: Long
    ): Flow<List<Task>> = taskDao.getTotalNumberOfCategoryToday(startTime, endTime)

    fun getTotalNumberOfCategoryFuture(
        currentTime: Long
    ): Flow<List<Task>> = taskDao.getTotalNumberOfCategoryFuture(currentTime)

    fun getTotalNumberOfCategoryCompleted(
        currentTime: Long
    ): Flow<List<Task>> = taskDao.getTotalNumberOfCategoryCompleted(currentTime)

    suspend fun insertTask(task: Task) = taskDao.insert(task)

    suspend fun deleteAllTask() = taskDao.deleteAllTask()

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun deleteTask(taskId: Int) = taskDao.deleteTask(taskId)

    suspend fun searchTask(key: String) = taskDao.searchTask(key)

}