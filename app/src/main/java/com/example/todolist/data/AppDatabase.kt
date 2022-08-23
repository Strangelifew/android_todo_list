package com.example.todolist.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todolist.model.TaskList

@Database(entities = [TaskList::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): TaskListDao
}