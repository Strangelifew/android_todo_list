package com.example.todolist.data.repository

import com.example.todolist.data.dao.TaskListDao
import com.example.todolist.data.model.toDBO
import com.example.todolist.data.model.toEntity
import com.example.todolist.domain.TaskListRepository
import com.example.todolist.domain.model.TaskList

class TaskListRepositoryImpl(
    private val taskListDao: TaskListDao
) : TaskListRepository {
    override fun addTaskList(list: TaskList) = taskListDao.insert(list.toDBO())

    override fun removeTaskList(listId: Int) = taskListDao.delete(listId)

    override fun updateTaskList(list: TaskList) = taskListDao.update(list.toDBO())

    override fun getTaskListOrNull(listId: Int): TaskList? =
        taskListDao.findById(listId)?.toEntity()

    override fun numberOfTasksInList(listId: Int): Int =
        taskListDao.countNumberOfTasksInList(listId)

    override val allTaskLists: List<TaskList> get() = taskListDao.all.map { it.toEntity() }
}