package com.example.todolist

import android.app.Application
import androidx.room.Room
import com.example.todolist.data.AppDatabase
import com.example.todolist.data.NoteDao

class App : Application() {
    var database: AppDatabase? = null
    lateinit var noteDao: NoteDao
    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app-db-name"
        )
            .allowMainThreadQueries()
            .build()
        noteDao = database!!.noteDao()
    }

    companion object {
        lateinit var instance: App
            private set
    }
}