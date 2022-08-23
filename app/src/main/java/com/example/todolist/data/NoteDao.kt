package com.example.todolist.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todolist.model.Note

@Dao
interface NoteDao {
    @get:Query("SELECT * FROM Note")
    val all: List<Note>

    @get:Query("SELECT * FROM Note")
    val allLiveData: LiveData<List<Note>>

    @Query("SELECT * FROM Note WHERE uid IN (:noteIds)")
    fun loadAllByIds(noteIds: IntArray): List<Note>

    @Query("SELECT * FROM Note WHERE uid = :uid LIMIT 1")
    fun findById(uid: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)
}