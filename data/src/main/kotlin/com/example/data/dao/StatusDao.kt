package com.example.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.data.model.StatusDBO

@Dao
interface StatusDao {
    @get:Query("SELECT * FROM StatusDBO ORDER BY sort_order, status_id LIMIT 1")
    val defaultStatus: StatusDBO

    @get:Query("SELECT * FROM StatusDBO ORDER BY sort_order, status_id")
    val all: List<StatusDBO>

    @get:Query("SELECT * FROM StatusDBO ORDER BY sort_order, status_id")
    val allLiveData: LiveData<List<StatusDBO>>

    @Query("SELECT * FROM StatusDBO WHERE status_id = :statusId LIMIT 1")
    fun findById(statusId: Int): StatusDBO

    @Query("SELECT COUNT(*) FROM TaskDBO WHERE status_id = :statusId")
    fun countNumberOfTasksByStatus(statusId: Int): Int

    @get:Query("SELECT status_id FROM StatusDBO ORDER BY sort_order, status_id LIMIT 1")
    val defaultStatusId: Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(status: StatusDBO)

    @Update
    fun update(status: StatusDBO)

    @Query("DELETE FROM StatusDBO WHERE status_id = :statusId")
    fun delete(statusId: Int)
}