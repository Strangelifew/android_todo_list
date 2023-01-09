package com.example.todolist.presentation.model

import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import com.example.todolist.domain.model.Status

data class StatusPO(
    var statusId: Int,
    var sortOrder: Int,
    var statusName: String,
    var statusColor: Color
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().orEmpty(),
        Color.valueOf(parcel.readInt())
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(statusId)
        parcel.writeInt(sortOrder)
        parcel.writeString(statusName)
        parcel.writeInt(statusColor.toArgb())
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<StatusPO> {
        override fun createFromParcel(parcel: Parcel): StatusPO = StatusPO(parcel)

        override fun newArray(size: Int): Array<StatusPO?> = arrayOfNulls(size)
    }
}

fun StatusPO.toEntity(): Status = Status(statusId, sortOrder, statusName, statusColor.toArgb())
fun Status.toPO(): StatusPO = StatusPO(statusId, sortOrder, statusName, Color.valueOf(statusColor))