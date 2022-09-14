package com.example.todolist

import android.app.Application
import androidx.room.Room
import com.example.todolist.data.AppDatabase
import com.example.todolist.data.TaskDao
import com.example.todolist.data.TaskListDao

class App : Application() {
    var database: AppDatabase? = null
    lateinit var taskListDao: TaskListDao
    lateinit var taskDao: TaskDao
    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app-db-name"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
            .apply {
                taskListDao = taskListDao()
                taskDao = taskDao()
            }
    }

    companion object {
        lateinit var instance: App
            private set
    }
}