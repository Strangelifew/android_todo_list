package com.example.todolist.screens.main

import androidx.lifecycle.ViewModel
import com.example.domain.model.TaskList
import com.example.todolist.App
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    val taskListsFlow: StateFlow<List<TaskList>> = App.taskListUseCases.allTaskLists
}