package com.example.todoapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM task_table")
    fun getAll(): List<Task>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(student: Task): Long

    @Update
    suspend fun updateTask(task: Task): Int

    @Query("DELETE FROM task_table WHERE id = :taskId")
    suspend fun deleteTask(taskId: Int): Int

    @Query("DELETE FROM task_table")
    suspend fun deleteAllTask()

    @Query("SELECT * FROM task_table WHERE id = :taskId")
    fun getTaskById(taskId: Int): Flow<Task>

    @Query("SELECT * FROM task_table")
    fun getTotalNumberOfCategoryAll(): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE time BETWEEN :startTime AND :endTime")
    fun getTotalNumberOfCategoryToday(
        startTime: Long,
        endTime: Long
    ): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE time > :currentTime")
    fun getTotalNumberOfCategoryFuture(
        currentTime: Long
    ): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE time < :currentTime")
    fun getTotalNumberOfCategoryCompleted(
        currentTime: Long
    ): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE title LIKE :key")
    suspend fun searchTask(key: String): List<Task>
}