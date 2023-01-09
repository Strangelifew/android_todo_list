package com.example.todolist.domain.usecase

import com.example.todolist.domain.StatusRepository
import com.example.todolist.domain.model.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class StatusUseCases(private val repository: StatusRepository) {
    fun removeStatus(statusId: Int) {
        repository.removeStatus(statusId)
        reloadStatuses()
    }

    fun getStatusOrNull(statusId: Int): Status? = repository.getStatusOrNull(statusId)

    fun changeStatusOrder(from: Int, to: Int) {
        val statuses = _allStatuses.value.toMutableList()
        val fromStatus = statuses[from]
        statuses.removeAt(from)
        statuses.add(to, fromStatus)
        statuses.forEachIndexed { index, status ->
            if (status.sortOrder != index) {
                repository.updateStatus(status.copy(sortOrder = index))
            }
        }
        reloadStatuses()
    }

    fun upsertStatus(status: Status) {
        repository.getStatusOrNull(status.statusId)?.let { repository.updateStatus(status) }
            ?: repository.addStatus(status)
        reloadStatuses()
    }

    fun isEmptyStatus(statusId: Int): Boolean = repository.numberOfTasksWithStatus(statusId) == 0

    val defaultStatus: Status by repository::defaultStatus
    val allStatuses: StateFlow<List<Status>> by ::_allStatuses

    private val _allStatuses: MutableStateFlow<List<Status>> =
        MutableStateFlow(repository.allStatuses)

    private fun reloadStatuses() {
        _allStatuses.update { repository.allStatuses }
    }
}