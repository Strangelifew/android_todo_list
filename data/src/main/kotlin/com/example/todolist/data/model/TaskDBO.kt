package com.example.todolist.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.todolist.domain.model.Status
import com.example.todolist.domain.model.Task
import com.example.todolist.domain.model.TaskList

@Entity
data class TaskDBO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    var taskId: Int = 0,
    @ColumnInfo(name = "list_id")
    @ForeignKey(
        entity = TaskListDBO::class,
        parentColumns = ["list_id"],
        childColumns = ["list_id"]
    )
    var listId: Int,
    @ColumnInfo(name = "status_id")
    @ForeignKey(
        entity = StatusDBO::class,
        parentColumns = ["status_id"],
        childColumns = ["status_id"]
    )
    var statusId: Int,
    @ColumnInfo(name = "description")
    var description: String = ""
)

fun TaskDBO.toEntity(taskList: TaskList, status: Status) =
    Task(taskId, taskList, status, description)

fun Task.toDBO() = TaskDBO(taskId, list.listId, status.statusId, description)