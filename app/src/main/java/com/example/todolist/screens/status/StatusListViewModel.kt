package com.example.todolist.screens.status

import androidx.lifecycle.ViewModel
import com.example.todolist.App

class StatusListViewModel : ViewModel() {
    val statusLiveData = App.statusDao.allLiveData
}