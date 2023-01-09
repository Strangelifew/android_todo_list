package com.example.todolist.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todolist.data.model.StatusDBO
import com.example.todolist.data.model.TaskDBO
import com.example.todolist.data.model.TaskListDBO

@Database(entities = [TaskListDBO::class, TaskDBO::class, StatusDBO::class], version = 8)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskListDao(): TaskListDao
    abstract fun taskDao(): TaskDao
    abstract fun statusDao(): StatusDao
}