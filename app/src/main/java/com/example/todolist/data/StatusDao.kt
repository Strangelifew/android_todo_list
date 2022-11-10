package com.example.todolist.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todolist.model.Status

@Dao
interface StatusDao {
    @get:Query("SELECT * FROM Status ORDER BY sort_order, status_id")
    val all: List<Status>

    @get:Query("SELECT * FROM Status ORDER BY sort_order, status_id")
    val allLiveData: LiveData<List<Status>>

    @Query("SELECT * FROM Status WHERE status_id = :statusId LIMIT 1")
    fun findById(statusId: Int): Status

    @Query("SELECT COUNT(*) FROM Task WHERE status_id = :statusId")
    fun countNumberOfTasksByStatus(statusId: Int): Int

    @get:Query("SELECT status_id FROM Status ORDER BY sort_order, status_id LIMIT 1")
    val defaultStatusId: Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(status: Status)

    @Update
    fun update(status: Status)

    @Delete
    fun delete(status: Status)
}