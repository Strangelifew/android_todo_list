package com.example.todolist.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.example.todolist.domain.model.TaskList

data class TaskListPO(
    var listId: Int,
    var listName: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().orEmpty()
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(listId)
        dest.writeString(listName)
    }

    companion object CREATOR : Parcelable.Creator<TaskListPO> {
        override fun createFromParcel(parcel: Parcel): TaskListPO = TaskListPO(parcel)

        override fun newArray(size: Int): Array<TaskListPO?> = arrayOfNulls(size)
    }
}

fun TaskListPO.toEntity(): TaskList = TaskList(listId, listName)
fun TaskList.toPO(): TaskListPO = TaskListPO(listId, listName)