package com.example.todolist

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.Color.*
import androidx.annotation.DoNotInline
import androidx.room.Room
import com.example.todolist.data.AppDatabase
import com.example.todolist.data.TaskDao
import com.example.todolist.data.TaskListDao
import com.example.todolist.model.Status
import com.example.todolist.model.StatusType.*

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
                        if (all.isEmpty()) {
                            insert(Status(0, 0, "Planned", Color.valueOf(GRAY), PLANNED))
                            insert(Status(1, 1, "In progress", Color.valueOf(BLUE), IN_PROGRESS))
                            insert(Status(2, 2, "Done", Color.valueOf(DKGRAY), DONE))
                        }
                    }

                }
        }
        val taskListDao: TaskListDao by lazy { database.taskListDao() }
        val taskDao: TaskDao by lazy { database.taskDao() }
    }
}