package com.example.domain

import com.example.domain.model.Task

interface TaskRepository {
    fun addTask(task: Task)
    fun removeTask(taskId: Int)
    fun updateTask(task: Task)
    fun getTaskStatusId(taskId: Int): Int
    fun tasksOfList(listId: Int): List<Task>
}