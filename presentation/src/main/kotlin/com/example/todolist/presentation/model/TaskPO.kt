package com.example.todolist.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.example.todolist.domain.model.Task

data class TaskPO(
    var taskId: Int,
    var list: TaskListPO,
    var status: StatusPO,
    var description: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readParcelable(TaskListPO::class.java.classLoader)!!,
        parcel.readParcelable(StatusPO::class.java.classLoader)!!,
        parcel.readString().orEmpty()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(taskId)
        parcel.writeParcelable(list, flags)
        parcel.writeParcelable(status, flags)
        parcel.writeString(description)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<TaskPO> {
        override fun createFromParcel(parcel: Parcel): TaskPO = TaskPO(parcel)

        override fun newArray(size: Int): Array<TaskPO?> = arrayOfNulls(size)
    }
}

fun TaskPO.toEntity(): Task = Task(taskId, list.toEntity(), status.toEntity(), description)
fun Task.toPO(): TaskPO = TaskPO(taskId, list.toPO(), status.toPO(), description)
