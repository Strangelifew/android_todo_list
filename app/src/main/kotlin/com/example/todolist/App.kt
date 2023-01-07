package com.example.todolist

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.Color.*
import androidx.room.Room
import com.example.data.dao.AppDatabase
import com.example.data.model.StatusDBO
import com.example.data.model.TaskDBO
import com.example.data.model.TaskListDBO
import com.example.data.repository.StatusRepositoryImpl
import com.example.data.repository.TaskListRepositoryImpl
import com.example.data.repository.TaskRepositoryImpl
import com.example.domain.model.Status
import com.example.domain.model.Task
import com.example.domain.model.TaskList
import com.example.domain.usecase.StatusUseCases
import com.example.domain.usecase.TaskListUseCases
import com.example.domain.usecase.TaskUseCases

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
                        @Suppress("RemoveRedundantQualifierName")
                        if (all.isEmpty()) {
                            insert(StatusDBO(1, 0, "Planned", Color.valueOf(GREEN)))
                            insert(StatusDBO(2, 1, "In progress", Color.valueOf(BLUE)))
                            insert(StatusDBO(3, 2, "Done", Color.valueOf(GRAY)))
                        }
                    }

                }
        }

        fun TaskListDBO.toEntity(): TaskList = TaskList(listId, listName)

        fun TaskDBO.toEntity(
            taskList: TaskList,
            status: Status
        ) = Task(taskId, taskList, status, description)

        fun StatusDBO.toEntity() = Status(statusId, sortOrder, statusName, statusColor.toArgb())

        fun TaskList.toDBO() = TaskListDBO(listId, listName)

        fun Task.toDBO() =
            TaskDBO(taskId, list.listId, status.statusId, description)

        @Suppress("RemoveRedundantQualifierName")
        fun Status.toDBO() = StatusDBO(statusId, sortOrder, statusName, Color.valueOf(statusColor))

        val taskListUseCases: TaskListUseCases by lazy {
            TaskListUseCases(
                taskListRepository
            )
        }

        val taskUseCases: TaskUseCases by lazy {
            TaskUseCases(
                TaskRepositoryImpl(
                    database.taskDao(),
                    taskListRepository,
                    statusRepository,
                    { taskList, status -> toEntity(taskList, status) },
                    { toDBO() }
                ),
                taskListRepository
            )
        }

        val statusUseCases: StatusUseCases by lazy {
            StatusUseCases(
                statusRepository
            )
        }

        private val taskListRepository by lazy {
            TaskListRepositoryImpl(
                database.taskListDao(),
                { toEntity() },
                { toDBO() }
            )
        }

        private val statusRepository by lazy {
            StatusRepositoryImpl(
                database.statusDao(),
                { toEntity() },
                { toDBO() }
            )
        }
    }
}