package com.example.todolist.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todolist.model.Status
import com.example.todolist.model.Task
import com.example.todolist.model.TaskList

@Dao
interface StatusDao {
    @get:Query("SELECT * FROM Status")
    val all: List<Status>

    @get:Query("SELECT * FROM Status")
    val allLiveData: LiveData<List<Status>>

    @Query("SELECT * FROM Status WHERE status_id IN (:statusIds)")
    fun loadAllByIds(statusIds: IntArray): List<Status>

    @Query("SELECT * FROM Status WHERE status_id = :statusId LIMIT 1")
    fun findById(statusId: Int): Status

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(status: Status)

    @Update
    fun update(status: Status)

    @Delete
    fun delete(status: Status)
}