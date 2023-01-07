package com.example.domain

import com.example.domain.model.Status

interface StatusRepository {
    fun addStatus(status: Status)
    fun removeStatus(statusId: Int)
    fun updateStatus(status: Status)
    val defaultStatus: Status
    val allStatuses: List<Status>
    fun numberOfTasksWithStatus(statusId: Int): Int
    fun getStatus(statusId: Int): Status
}