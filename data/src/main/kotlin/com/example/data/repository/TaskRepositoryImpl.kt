package com.example.data.repository

import com.example.data.dao.TaskDao
import com.example.data.model.TaskDBO
import com.example.domain.StatusRepository
import com.example.domain.TaskListRepository
import com.example.domain.TaskRepository
import com.example.domain.model.Status
import com.example.domain.model.Task
import com.example.domain.model.TaskList

class TaskRepositoryImpl(
    private val taskDao: TaskDao,
    private val taskListRepository: TaskListRepository,
    private val statusRepository: StatusRepository,
    private val toEntity: TaskDBO.(TaskList, Status) -> Task,
    private val toDBO: Task.() -> TaskDBO
) : TaskRepository {

    override fun addTask(task: Task) = taskDao.insert(task.toDBO())

    override fun removeTask(taskId: Int) = taskDao.delete(taskId)

    override fun updateTask(task: Task) = taskDao.update(task.toDBO())

    override fun getTaskStatusId(taskId: Int): Int = taskDao.getTaskStatusId(taskId)
    override fun tasksOfList(listId: Int): List<Task> {
        val taskList = taskListRepository.getTaskListOrNull(listId)!!
        return taskDao.findByListId(listId)
            .map { it.toEntity(taskList, statusRepository.getStatus(it.statusId)) }
    }
}