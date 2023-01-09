package com.example.todolist.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todolist.domain.model.TaskList

@Entity
data class TaskListDBO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "list_id")
    var listId: Int = 0,
    @ColumnInfo(name = "list_name")
    var listName: String = ""
)

fun TaskListDBO.toEntity(): TaskList = TaskList(listId, listName)

fun TaskList.toDBO() = TaskListDBO(listId, listName)