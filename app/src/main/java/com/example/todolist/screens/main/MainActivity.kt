package com.example.todolist.screens.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.screens.details.TaskListDetailsActivity.Companion.startTaskListDetails
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var  recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.main_list)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        val adapter = MainAdapter()
        recyclerView.adapter = adapter
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { startTaskListDetails(this@MainActivity, null) }
        val mainViewModel = ViewModelProviders.of(this).get(
            MainViewModel::class.java
        )
        mainViewModel.taskListLiveData.observe(this) { taskLists -> adapter.setItems(taskLists) }
    }
}