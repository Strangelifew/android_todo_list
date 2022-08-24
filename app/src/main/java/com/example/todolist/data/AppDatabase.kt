package com.example.todolist.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todolist.model.Status
import com.example.todolist.model.Task
import com.example.todolist.model.TaskList

@Database(entities = [TaskList::class, Task::class, Status::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tasListDao(): TaskListDao
    abstract fun taskDao(): TaskDao
    abstract fun statusDao(): StatusDao
}