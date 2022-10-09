package com.example.todolist.model

import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

class ColorConverter {
    @TypeConverter
    fun toColor(value: Int): Color = Color.valueOf(value)

    @TypeConverter
    fun fromColor(value: Color): Int = value.toArgb()
}

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
    var statusColor: Color
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().orEmpty(),
        Color.valueOf(parcel.readInt()),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(statusId)
        parcel.writeInt(sortOrder)
        parcel.writeString(statusName)
        parcel.writeInt(statusColor.toArgb())
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