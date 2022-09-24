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
import com.example.todolist.App
import com.example.todolist.R
import com.example.todolist.model.TaskList
import com.example.todolist.screens.details.TaskDetailsActivity.Companion.startTaskDetails
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.todolist.screens.details.TaskListDetailsActivity.Companion.startTaskListDetails

class TaskListActivity : AppCompatActivity() {
    val taskList: TaskList by lazy { intent.getParcelableExtra(EXTRA_TASK_LIST)!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_task_list)

        val adapter = TaskListAdapter(taskList)

        findViewById<RecyclerView>(R.id.task_list).also {
            it.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            it.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
            it.adapter = adapter
        }

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            startTaskDetails(this, taskList, null)
        }

        findViewById<FloatingActionButton>(R.id.edit).setOnClickListener {
            startTaskListDetails(this, taskList)
        }

        ViewModelProviders.of(this).get(
            TaskListViewModel::class.java
        ).findByListId(taskList.listId).observe(this, adapter::setItems)
    }

    override fun onResume() {
        super.onResume()

        App.taskListDao.findById(taskList.listId)?.listName?.let { title = it }
    }

    companion object {
        private const val EXTRA_TASK_LIST = "TaskListActivity.EXTRA_TASK_LIST"

        @JvmStatic
        fun startTaskList(caller: Activity, taskList: TaskList) {
            val intent = Intent(caller, TaskListActivity::class.java)
            intent.putExtra(EXTRA_TASK_LIST, taskList)
            caller.startActivity(intent)
        }
    }
}