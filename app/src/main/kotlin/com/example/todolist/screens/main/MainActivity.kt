package com.example.todolist.screens.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.App.Companion.toDBO
import com.example.todolist.R
import com.example.todolist.screens.details.TaskListDetailsActivity.Companion.startTaskListDetails
import com.example.todolist.screens.status.StatusListActivity.Companion.startStatusList
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private val adapter = MainAdapter()
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

        findViewById<FloatingActionButton>(R.id.edit_statuses).setOnClickListener {
            startStatusList(this)
        }

        val viewModel = ViewModelProviders.of(this).get(
            MainViewModel::class.java
        )

        lifecycleScope.launchWhenStarted {
            viewModel.taskListsFlow.collect { taskLists ->
                adapter.setItems(taskLists.map { it.toDBO() })
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()

        adapter.notifyDataSetChanged()
    }
}