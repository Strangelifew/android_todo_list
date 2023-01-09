package com.example.todolist.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todolist.domain.model.Status

@Entity
data class StatusDBO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "status_id")
    var statusId: Int = 0,
    @ColumnInfo(name = "sort_order")
    var sortOrder: Int,
    @ColumnInfo(name = "status_name")
    var statusName: String,
    @ColumnInfo(name = "status_color")
    var statusColor: Int
)

fun StatusDBO.toEntity() = Status(statusId, sortOrder, statusName, statusColor)

fun Status.toDBO() = StatusDBO(statusId, sortOrder, statusName, statusColor)