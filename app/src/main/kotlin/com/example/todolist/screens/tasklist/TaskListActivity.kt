package com.example.todolist.screens.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.data.model.TaskListDBO
import com.example.todolist.App
import com.example.todolist.App.Companion.toDBO
import com.example.todolist.R
import com.example.todolist.screens.details.TaskDetailsActivity.Companion.startTaskDetails
import com.example.todolist.screens.details.TaskListDetailsActivity.Companion.startTaskListDetails
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TaskListActivity : AppCompatActivity() {
    val taskList: TaskListDBO by lazy { intent.getParcelableExtra(EXTRA_TASK_LIST)!! }

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

        val viewModel = ViewModelProviders.of(this).get(
            TaskListViewModel::class.java
        )

        lifecycleScope.launchWhenStarted {
            viewModel.tasksFlowOfList(taskList.listId).collect { tasks ->
                adapter.setItems(tasks.map { it.toDBO() })
            }
        }
    }

    override fun onResume() {
        super.onResume()

        App.taskListUseCases.getTaskListOrNull(taskList.listId)?.listName?.let { title = it }
    }

    companion object {
        private const val EXTRA_TASK_LIST = "TaskListActivity.EXTRA_TASK_LIST"

        @JvmStatic
        fun startTaskList(caller: Activity, taskList: TaskListDBO) {
            val intent = Intent(caller, TaskListActivity::class.java)
            intent.putExtra(EXTRA_TASK_LIST, taskList)
            caller.startActivity(intent)
        }
    }
}