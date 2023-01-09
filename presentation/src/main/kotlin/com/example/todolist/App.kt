package com.example.todolist

import android.app.Application
import android.content.Context
import android.graphics.Color.*
import androidx.room.Room
import com.example.todolist.data.dao.AppDatabase
import com.example.todolist.data.model.StatusDBO
import com.example.todolist.data.repository.StatusRepositoryImpl
import com.example.todolist.data.repository.TaskListRepositoryImpl
import com.example.todolist.data.repository.TaskRepositoryImpl
import com.example.todolist.domain.usecase.StatusUseCases
import com.example.todolist.domain.usecase.TaskListUseCases
import com.example.todolist.domain.usecase.TaskUseCases

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
                            insert(StatusDBO(1, 0, "Planned", GREEN))
                            insert(StatusDBO(2, 1, "In progress", BLUE))
                            insert(StatusDBO(3, 2, "Done", GRAY))
                        }
                    }
                }
        }

        val taskListUseCases: TaskListUseCases by lazy { TaskListUseCases(taskListRepository) }

        val taskUseCases: TaskUseCases by lazy {
            TaskUseCases(
                TaskRepositoryImpl(database.taskDao(), taskListRepository, statusRepository),
                taskListRepository
            )
        }

        val statusUseCases: StatusUseCases by lazy { StatusUseCases(statusRepository) }

        private val taskListRepository by lazy { TaskListRepositoryImpl(database.taskListDao()) }

        private val statusRepository by lazy { StatusRepositoryImpl(database.statusDao()) }
    }
}