package com.example.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.data.model.TaskDBO

@Dao
interface TaskDao {
    @Query("SELECT * FROM TaskDBO WHERE list_id = :listId")
    fun findByListIdLD(listId: Int): LiveData<List<TaskDBO>>

    @Query("SELECT * FROM TaskDBO WHERE list_id = :listId")
    fun findByListId(listId: Int): List<TaskDBO>

    @Query("SELECT status_id FROM TaskDBO WHERE task_id = :taskId")
    fun getTaskStatusId(taskId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: TaskDBO)

    @Update
    fun update(task: TaskDBO)

    @Query("DELETE FROM TaskDBO WHERE task_id = :taskId")
    fun delete(taskId: Int)
}