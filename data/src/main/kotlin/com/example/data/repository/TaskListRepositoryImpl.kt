package com.example.data.repository

import com.example.data.dao.TaskListDao
import com.example.data.model.TaskListDBO
import com.example.domain.TaskListRepository
import com.example.domain.model.TaskList

class TaskListRepositoryImpl(
    private val taskListDao: TaskListDao,
    private val toEntity: TaskListDBO.() -> TaskList,
    private val toDBO: TaskList.() -> TaskListDBO
) : TaskListRepository {
    override fun addTaskList(list: TaskList) = taskListDao.insert(list.toDBO())

    override fun removeTaskList(listId: Int) = taskListDao.delete(listId)

    override fun updateTaskList(list: TaskList) = taskListDao.update(list.toDBO())

    override fun getTaskListOrNull(listId: Int): TaskList? =
        taskListDao.findById(listId)?.let(toEntity)

    override fun numberOfTasksInList(listId: Int): Int =
        taskListDao.countNumberOfTasksInList(listId)

    override val allTaskLists: List<TaskList> get() = taskListDao.all.map(toEntity)
}