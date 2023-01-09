package com.example.todolist.domain.usecase

import com.example.todolist.domain.TaskListRepository
import com.example.todolist.domain.TaskRepository
import com.example.todolist.domain.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.ConcurrentHashMap

class TaskUseCases(
    private val taskRepository: TaskRepository,
    taskListRepository: TaskListRepository
) {
    fun removeTask(taskId: Int, listId: Int) {
        taskRepository.removeTask(taskId)
        reloadTasks(listId)
    }

    fun updateTask(task: Task) {
        taskRepository.updateTask(task)
        reloadTasks(task.list.listId)
    }

    fun tasksOfList(listId: Int): List<Task> = taskRepository.tasksOfList(listId)

    fun upsertTask(task: Task) {
        taskRepository.getTaskOrNull(task.taskId)?.let { taskRepository.updateTask(task) }
            ?: taskRepository.addTask(task)
        reloadTasks(task.list.listId)
    }

    fun tasksFlowOfList(listId: Int): StateFlow<List<Task>> =
        _tasksToListId.computeIfAbsent(listId) {
            MutableStateFlow(taskRepository.tasksOfList(listId))
        }

    private val _tasksToListId: MutableMap<Int, MutableStateFlow<List<Task>>> =
        taskListRepository.allTaskLists.associateTo(ConcurrentHashMap()) {
            it.listId to MutableStateFlow(taskRepository.tasksOfList(it.listId))
        }

    private fun reloadTasks(listId: Int) {
        _tasksToListId.compute(listId) { k, flow ->
            flow?.apply { update { taskRepository.tasksOfList(k) } }
                ?: MutableStateFlow(taskRepository.tasksOfList(k))
        }
    }
}