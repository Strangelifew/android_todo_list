package com.example.todolist.screens.tasklist

import androidx.lifecycle.ViewModel
import com.example.todolist.App

class TaskListViewModel : ViewModel() {
    val taskLiveData = App.instance.taskDao.allLiveData
}