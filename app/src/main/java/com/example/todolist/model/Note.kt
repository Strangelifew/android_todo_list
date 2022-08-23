package com.example.todolist.model

import android.os.Parcelable
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import android.os.Parcel
import android.os.Parcelable.Creator
import androidx.room.Entity

@Entity
data class Note(
    @JvmField
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,
    @JvmField
    @ColumnInfo(name = "text")
    var text: String,
    @JvmField
    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0,
    @JvmField
    @ColumnInfo(name = "done")
    var done: Boolean = false
) : Parcelable {

    constructor() : this(text = "")

    protected constructor (`in`: Parcel) : this(
        uid = `in`.readInt(),
        text = `in`.readString().orEmpty(),
        timestamp = `in`.readLong(),
        done = `in`.readByte().toInt() != 0
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(uid)
        dest.writeString(text)
        dest.writeLong(timestamp)
        dest.writeByte((if (done) 1 else 0).toByte())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Creator<Note> = object : Creator<Note> {
            override fun createFromParcel(`in`: Parcel): Note = Note(`in`)

            override fun newArray(size: Int): Array<Note?> = arrayOfNulls(size)
        }
    }
}