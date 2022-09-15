package com.example.todolist.screens.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.model.TaskList
import com.example.todolist.screens.details.TaskDetailsActivity.Companion.startTaskDetails
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TaskListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        recyclerView = findViewById(R.id.task_list)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        val taskList: TaskList = intent.getParcelableExtra(EXTRA_TASK_LIST)!!
        val adapter = TaskListAdapter(taskList)
        recyclerView.adapter = adapter
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            startTaskDetails(this, taskList, null)
        }
        val mainViewModel = ViewModelProviders.of(this).get(
            TaskListViewModel::class.java
        )
        mainViewModel.findByListId(taskList.listId).observe(this) { tasks -> adapter.setItems(tasks) }
    }

    companion object {
        private const val EXTRA_TASK_LIST = "NoteDetailsActivity.EXTRA_TASK_LIST"

        @JvmStatic
        fun startTaskList(caller: Activity, taskList: TaskList) {
            val intent = Intent(caller, TaskListActivity::class.java)
            intent.putExtra(EXTRA_TASK_LIST, taskList)
            caller.startActivity(intent)
        }
    }
}