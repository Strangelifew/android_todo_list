package com.example.todolist.screens.tasklist

import androidx.lifecycle.ViewModel
import com.example.domain.model.Task
import com.example.todolist.App
import kotlinx.coroutines.flow.StateFlow

class TaskListViewModel : ViewModel() {
    fun tasksFlowOfList(listId: Int): StateFlow<List<Task>> =
        App.taskUseCases.tasksFlowOfList(listId)
}