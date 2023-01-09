package com.example.todolist.domain.model

data class Status(
    val statusId: Int,
    val sortOrder: Int,
    val statusName: String,
    val statusColor: Int
)