package com.example.domain.model

data class Task(
    val taskId: Int,
    val list: TaskList,
    val status: Status,
    val description: String = ""
)
