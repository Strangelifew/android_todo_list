package com.example.todolist.domain

import com.example.todolist.domain.model.Status

interface StatusRepository {
    fun addStatus(status: Status)
    fun removeStatus(statusId: Int)
    fun updateStatus(status: Status)
    val defaultStatus: Status
    val allStatuses: List<Status>
    fun numberOfTasksWithStatus(statusId: Int): Int
    fun getStatusOrNull(statusId: Int): Status?
}