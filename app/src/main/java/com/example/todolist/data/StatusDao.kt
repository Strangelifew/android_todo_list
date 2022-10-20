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

    @Query("SELECT COUNT(*) FROM Task WHERE status_id = :statusId")
    fun countNumberOfTasksByStatus(statusId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(status: Status)

    @Update
    fun update(status: Status)

    @Delete
    fun delete(status: Status)
}