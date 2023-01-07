package com.example.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().orEmpty()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(taskId)
        parcel.writeInt(listId)
        parcel.writeInt(statusId)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskDBO> {
        override fun createFromParcel(parcel: Parcel): TaskDBO {
            return TaskDBO(parcel)
        }

        override fun newArray(size: Int): Array<TaskDBO?> {
            return arrayOfNulls(size)
        }
    }
}