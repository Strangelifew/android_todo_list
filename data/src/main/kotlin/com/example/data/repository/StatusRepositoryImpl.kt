package com.example.data.repository

import com.example.data.dao.StatusDao
import com.example.data.model.StatusDBO
import com.example.domain.StatusRepository
import com.example.domain.model.Status

class StatusRepositoryImpl(
    private val statusDao: StatusDao,
    private val toEntity: StatusDBO.() -> Status,
    private val toDBO: Status.() -> StatusDBO
) : StatusRepository {
    override fun addStatus(status: Status) = statusDao.insert(status.toDBO())

    override fun removeStatus(statusId: Int) = statusDao.delete(statusId)

    override fun updateStatus(status: Status) = statusDao.update(status.toDBO())
    override val defaultStatus: Status get() = statusDao.defaultStatus.toEntity()

    override val allStatuses: List<Status> get() = statusDao.all.map(toEntity)

    override fun numberOfTasksWithStatus(statusId: Int): Int =
        statusDao.countNumberOfTasksByStatus(statusId)

    override fun getStatus(statusId: Int): Status = statusDao.findById(statusId).toEntity()
}