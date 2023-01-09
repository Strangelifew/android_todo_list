package com.example.todolist.domain

import com.example.todolist.domain.model.TaskList

interface TaskListRepository {
    fun addTaskList(list: TaskList)
    fun removeTaskList(listId: Int)
    fun updateTaskList(list: TaskList)
    fun getTaskListOrNull(listId: Int): TaskList?
    fun numberOfTasksInList(listId: Int): Int
    val allTaskLists: List<TaskList>
}