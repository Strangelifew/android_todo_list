package com.example.todolist.data.repository

import com.example.todolist.data.dao.TaskDao
import com.example.todolist.data.model.toDBO
import com.example.todolist.data.model.toEntity
import com.example.todolist.domain.StatusRepository
import com.example.todolist.domain.TaskListRepository
import com.example.todolist.domain.TaskRepository
import com.example.todolist.domain.model.Task

class TaskRepositoryImpl(
    private val taskDao: TaskDao,
    private val taskListRepository: TaskListRepository,
    private val statusRepository: StatusRepository
) : TaskRepository {
    override fun addTask(task: Task) = taskDao.insert(task.toDBO())

    override fun removeTask(taskId: Int) = taskDao.delete(taskId)

    override fun updateTask(task: Task) = taskDao.update(task.toDBO())

    override fun getTaskStatusId(taskId: Int): Int = taskDao.getTaskStatusId(taskId)

    override fun tasksOfList(listId: Int): List<Task> {
        val taskList = taskListRepository.getTaskListOrNull(listId)!!
        return taskDao.findByListId(listId)
            .map { it.toEntity(taskList, statusRepository.getStatusOrNull(it.statusId)!!) }
    }

    override fun getTaskOrNull(taskId: Int): Task? =
        taskDao.findByIdOrEmpty(taskId).singleOrNull()?.run {
            toEntity(
                taskListRepository.getTaskListOrNull(listId)!!,
                statusRepository.getStatusOrNull(statusId)!!
            )
        }
}