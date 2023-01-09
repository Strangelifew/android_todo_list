package com.example.todolist.presentation.screens.tasklist

import androidx.lifecycle.ViewModel
import com.example.todolist.App
import com.example.todolist.domain.model.Task
import kotlinx.coroutines.flow.StateFlow

class TaskListViewModel : ViewModel() {
    fun tasksFlowOfList(listId: Int): StateFlow<List<Task>> =
        App.taskUseCases.tasksFlowOfList(listId)
}