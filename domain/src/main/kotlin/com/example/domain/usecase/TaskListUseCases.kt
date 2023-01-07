package com.example.domain.usecase

import com.example.domain.TaskListRepository
import com.example.domain.model.TaskList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class TaskListUseCases(private val repository: TaskListRepository) {
    fun addTaskList(list: TaskList) {
        repository.addTaskList(list)
        reloadTaskLists()
    }

    fun removeTaskList(listId: Int) {
        repository.removeTaskList(listId)
        reloadTaskLists()
    }

    fun updateTaskList(list: TaskList) {
        repository.updateTaskList(list)
        reloadTaskLists()
    }

    fun getTaskListOrNull(listId: Int): TaskList? = repository.getTaskListOrNull(listId)

    fun numberOfTasksInList(listId: Int): Int = repository.numberOfTasksInList(listId)

    val allTaskLists: StateFlow<List<TaskList>> by ::_allTaskLists

    private val _allTaskLists: MutableStateFlow<List<TaskList>> =
        MutableStateFlow(repository.allTaskLists)

    private fun reloadTaskLists() {
        _allTaskLists.update { repository.allTaskLists }
    }
}