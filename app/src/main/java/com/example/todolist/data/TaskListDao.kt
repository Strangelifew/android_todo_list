package com.example.todolist.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todolist.model.TaskList

@Dao
interface TaskListDao {

    @get:Query("SELECT * FROM TaskList")
    val allLiveData: LiveData<List<TaskList>>

    @Query("SELECT * FROM TaskList WHERE list_id = :listId LIMIT 1")
    fun findById(listId: Int): TaskList?

    @Query("SELECT COUNT(*) FROM Task WHERE list_id = :listId")
    fun countNumberOfTasksInList(listId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(taskList: TaskList)

    @Update
    fun update(taskList: TaskList)

    @Delete
    fun delete(taskList: TaskList)
}