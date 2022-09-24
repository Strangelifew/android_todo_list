package com.example.todolist.model

import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

enum class StatusType { PLANNED, IN_PROGRESS, DONE }

class StatusTypeConverter {
    @TypeConverter
    fun toStatusType(value: String): StatusType = enumValueOf(value)

    @TypeConverter
    fun fromStatusType(value: StatusType): String = "$value"
}

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
    var statusColor: Color,
    @ColumnInfo(name = "status_type")
    var statusType: StatusType = StatusType.PLANNED
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().orEmpty(),
        Color.valueOf(parcel.readInt()),
        StatusType.valueOf(parcel.readString().orEmpty())
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(statusId)
        parcel.writeInt(sortOrder)
        parcel.writeString(statusName)
        parcel.writeInt(statusColor.toArgb())
        parcel.writeString("$statusType")
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