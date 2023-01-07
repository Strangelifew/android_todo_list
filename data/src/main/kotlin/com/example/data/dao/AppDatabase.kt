package com.example.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.ColorConverter
import com.example.data.model.StatusDBO
import com.example.data.model.TaskDBO
import com.example.data.model.TaskListDBO

@Database(entities = [TaskListDBO::class, TaskDBO::class, StatusDBO::class], version = 8)
@TypeConverters(ColorConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskListDao(): TaskListDao
    abstract fun taskDao(): TaskDao
    abstract fun statusDao(): StatusDao
}