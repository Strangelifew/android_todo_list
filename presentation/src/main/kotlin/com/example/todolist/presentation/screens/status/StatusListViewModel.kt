package com.example.todolist.presentation.screens.status

import androidx.lifecycle.ViewModel
import com.example.todolist.App
import com.example.todolist.domain.model.Status
import kotlinx.coroutines.flow.StateFlow

class StatusListViewModel : ViewModel() {
    val statusesFlow: StateFlow<List<Status>> = App.statusUseCases.allStatuses
}