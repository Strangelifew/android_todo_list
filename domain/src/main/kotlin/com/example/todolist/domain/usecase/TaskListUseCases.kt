package com.example.todolist.domain.usecase

import com.example.todolist.domain.TaskListRepository
import com.example.todolist.domain.model.TaskList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class TaskListUseCases(private val repository: TaskListRepository) {
    fun removeTaskList(listId: Int) {
        repository.removeTaskList(listId)
        reloadTaskLists()
    }

    fun numberOfTasksInList(listId: Int): Int = repository.numberOfTasksInList(listId)

    fun upsertTaskList(taskList: TaskList) {
        repository.getTaskListOrNull(taskList.listId)?.let { repository.updateTaskList(taskList) }
            ?: repository.addTaskList(taskList)
        reloadTaskLists()
    }

    val allTaskLists: StateFlow<List<TaskList>> by ::_allTaskLists

    private val _allTaskLists: MutableStateFlow<List<TaskList>> =
        MutableStateFlow(repository.allTaskLists)

    private fun reloadTaskLists() {
        _allTaskLists.update { repository.allTaskLists }
    }
}