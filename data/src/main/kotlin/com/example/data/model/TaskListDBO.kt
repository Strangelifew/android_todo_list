package com.example.data.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskListDBO(
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
        val CREATOR: Creator<TaskListDBO> = object : Creator<TaskListDBO> {
            override fun createFromParcel(`in`: Parcel): TaskListDBO = TaskListDBO(`in`)

            override fun newArray(size: Int): Array<TaskListDBO?> = arrayOfNulls(size)
        }
    }
}