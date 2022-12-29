package com.example.todoapp.di

import android.content.Context
import com.example.todoapp.data.database.TaskDB
import com.example.todoapp.data.database.TaskDao
import com.example.todoapp.data.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DBModule {
    @Provides
    fun provideTaskDao(@ApplicationContext context: Context) : TaskDao {
        return TaskDB.getInstance(context).taskDao
    }

    @Provides
    fun provideTaskDBRepository(taskDao: TaskDao) = TaskRepository(taskDao)
}