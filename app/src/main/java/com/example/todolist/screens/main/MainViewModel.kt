package com.example.todolist.screens.main

import androidx.lifecycle.ViewModel
import com.example.todolist.App

class MainViewModel : ViewModel() {
    val taskListLiveData = App.taskListDao.allLiveData
}