package com.example.todolist.screens.tasklist

import androidx.lifecycle.ViewModel
import com.example.todolist.App

class TaskListViewModel : ViewModel() {
    fun findByListId(listId: Int) = App.taskDao.findByListId(listId)
}