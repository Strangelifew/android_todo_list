package com.example.todolist.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Status(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "status_id")
    var statusId: Int = 0,
    @ColumnInfo(name = "sort_order")
    var sortOrder: Int,
    @ColumnInfo(name = "status_name")
    var statusName: String,
    @ColumnInfo(name = "status_color")
    var statusColor: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().orEmpty(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(statusId)
        parcel.writeInt(sortOrder)
        parcel.writeString(statusName)
        parcel.writeInt(statusColor)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Status> {
        override fun createFromParcel(parcel: Parcel): Status {
            return Status(parcel)
        }

        override fun newArray(size: Int): Array<Status?> {
            return arrayOfNulls(size)
        }
    }
}