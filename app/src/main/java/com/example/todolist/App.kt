package com.example.todolist

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.Color.*
import androidx.room.Room
import com.example.todolist.data.AppDatabase
import com.example.todolist.data.StatusDao
import com.example.todolist.data.TaskDao
import com.example.todolist.data.TaskListDao
import com.example.todolist.model.Status

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        App.applicationContext = applicationContext
    }

    companion object {
        private lateinit var applicationContext: Context
        private val database: AppDatabase by lazy {
            Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "app-db-name"
            )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .also {
                    it.statusDao().run {
//                        all.forEach(::delete)
                        if (all.isEmpty()) {
                            insert(Status(1, 0, "Planned", Color.valueOf(GREEN)))
                            insert(Status(2, 1, "In progress", Color.valueOf(BLUE)))
                            insert(Status(3, 2, "Done", Color.valueOf(GRAY)))
                        }
                    }

                }
        }
        val taskListDao: TaskListDao by lazy { database.taskListDao() }
        val taskDao: TaskDao by lazy { database.taskDao() }
        val statusDao: StatusDao by lazy { database.statusDao() }
    }
}