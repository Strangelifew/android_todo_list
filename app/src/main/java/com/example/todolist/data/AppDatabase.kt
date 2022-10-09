package com.example.todolist.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todolist.model.*

@Database(entities = [TaskList::class, Task::class, Status::class], version = 6)
@TypeConverters(ColorConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskListDao(): TaskListDao
    abstract fun taskDao(): TaskDao
    abstract fun statusDao(): StatusDao
}