package com.example.todolist.data.repository

import com.example.todolist.data.dao.StatusDao
import com.example.todolist.data.model.toDBO
import com.example.todolist.data.model.toEntity
import com.example.todolist.domain.StatusRepository
import com.example.todolist.domain.model.Status

class StatusRepositoryImpl(
    private val statusDao: StatusDao
) : StatusRepository {
    override fun addStatus(status: Status) = statusDao.insert(status.toDBO())

    override fun removeStatus(statusId: Int) = statusDao.delete(statusId)

    override fun updateStatus(status: Status) = statusDao.update(status.toDBO())
    override val defaultStatus: Status get() = statusDao.defaultStatus.toEntity()

    override val allStatuses: List<Status> get() = statusDao.all.map { it.toEntity() }

    override fun numberOfTasksWithStatus(statusId: Int): Int =
        statusDao.countNumberOfTasksByStatus(statusId)

    override fun getStatusOrNull(statusId: Int): Status? =
        statusDao.findByIdOrEmpty(statusId).singleOrNull()?.toEntity()
}