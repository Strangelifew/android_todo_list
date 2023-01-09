package com.example.todolist.data.dao

import androidx.room.*
import com.example.todolist.data.model.TaskDBO

@Dao
interface TaskDao {
    @Query("SELECT * FROM TaskDBO WHERE list_id = :listId")
    fun findByListId(listId: Int): List<TaskDBO>

    @Query("SELECT status_id FROM TaskDBO WHERE task_id = :taskId")
    fun getTaskStatusId(taskId: Int): Int

    @Query("SELECT * FROM TaskDBO WHERE task_id = :taskId LIMIT 1")
    fun findByIdOrEmpty(taskId: Int): List<TaskDBO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: TaskDBO)

    @Update
    fun update(task: TaskDBO)

    @Query("DELETE FROM TaskDBO WHERE task_id = :taskId")
    fun delete(taskId: Int)
}