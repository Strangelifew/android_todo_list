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
    private val adapter = MainAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.main_list).also {
            it.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            it.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
            it.adapter = adapter
        }

        findViewById<Toolbar>(R.id.toolbar).also(::setSupportActionBar)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            startTaskListDetails(this, null)
        }

        ViewModelProviders.of(this).get(
            MainViewModel::class.java
        ).taskListLiveData.observe(this, adapter::setItems)
    }

    override fun onResume() {
        super.onResume()

        adapter.notifyDataSetChanged()
    }
}