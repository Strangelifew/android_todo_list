package com.example.todolist.screens.status

import androidx.lifecycle.ViewModel
import com.example.domain.model.Status
import com.example.todolist.App
import kotlinx.coroutines.flow.StateFlow

class StatusListViewModel : ViewModel() {
    val statusesFlow: StateFlow<List<Status>> = App.statusUseCases.allStatuses
}