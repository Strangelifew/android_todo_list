package com.example.data.dao

import androidx.room.*
import com.example.data.model.TaskListDBO

@Dao
interface TaskListDao {
    @get:Query("SELECT * FROM TaskListDBO")
    val all: List<TaskListDBO>

    @Query("SELECT * FROM TaskListDBO WHERE list_id = :listId LIMIT 1")
    fun findById(listId: Int): TaskListDBO?

    @Query("SELECT COUNT(*) FROM TaskDBO WHERE list_id = :listId")
    fun countNumberOfTasksInList(listId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(taskList: TaskListDBO)

    @Update
    fun update(taskList: TaskListDBO)

    @Query("DELETE FROM TaskListDBO WHERE list_id = :listId")
    fun delete(listId: Int)
}