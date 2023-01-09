package com.example.todolist.presentation.screens.main

import androidx.lifecycle.ViewModel
import com.example.todolist.App
import com.example.todolist.domain.model.TaskList
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    val taskListsFlow: StateFlow<List<TaskList>> = App.taskListUseCases.allTaskLists
}