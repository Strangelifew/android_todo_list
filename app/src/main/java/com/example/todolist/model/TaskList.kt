package com.example.todolist.model

import android.os.Parcelable
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import android.os.Parcel
import android.os.Parcelable.Creator
import androidx.room.Entity

@Entity
data class TaskList(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "list_id")
    var listId: Int = 0,
    @ColumnInfo(name = "list_name")
    var listName: String = ""
) : Parcelable {

    protected constructor (`in`: Parcel) : this(
        listId = `in`.readInt(),
        listName = `in`.readString().orEmpty()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(listId)
        dest.writeString(listName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Creator<TaskList> = object : Creator<TaskList> {
            override fun createFromParcel(`in`: Parcel): TaskList = TaskList(`in`)

            override fun newArray(size: Int): Array<TaskList?> = arrayOfNulls(size)
        }
    }
}