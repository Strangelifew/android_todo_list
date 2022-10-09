package com.example.todolist.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todolist.model.Status
import com.example.todolist.model.Task
import com.example.todolist.model.TaskList

@Dao
interface TaskDao {
    @get:Query("SELECT * FROM Task")
    val all: List<Task>

    @Query("SELECT * FROM Task WHERE task_id IN (:taskIds)")
    fun loadAllByIds(taskIds: IntArray): List<Task>

    @Query("SELECT * FROM Task WHERE task_id = :taskId LIMIT 1")
    fun findById(taskId: Int): Task

    @Query("SELECT * FROM Task WHERE list_id = :listId")
    fun findByListId(listId: Int): LiveData<List<Task>>

    @Query("SELECT status_id FROM Task WHERE task_id = :taskId")
    fun getTaskStatusId(taskId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)
}