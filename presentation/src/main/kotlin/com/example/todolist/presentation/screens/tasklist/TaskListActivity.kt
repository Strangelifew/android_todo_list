package com.example.todolist.presentation.screens.tasklist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.presentation.model.TaskListPO
import com.example.todolist.presentation.model.toPO
import com.example.todolist.presentation.screens.details.TaskDetailsActivity.Companion.startTaskDetails
import com.example.todolist.presentation.screens.details.TaskListDetailsActivity.Companion.EXTRA_TASK_LIST_RESULT
import com.example.todolist.presentation.screens.details.TaskListDetailsActivity.Companion.taskListDetailsIntent
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TaskListActivity : AppCompatActivity() {
    lateinit var taskList: TaskListPO
    private val taskListDetailLauncher = registerForActivityResult(
        object : ActivityResultContract<TaskListPO, TaskListPO?>() {
            override fun createIntent(context: Context, input: TaskListPO): Intent =
                taskListDetailsIntent(this@TaskListActivity, input)

            override fun parseResult(resultCode: Int, intent: Intent?): TaskListPO? =
                intent?.takeIf { resultCode == RESULT_OK }
                    ?.getParcelableExtra(EXTRA_TASK_LIST_RESULT)

        }
    ) {
        it?.let {
            taskList = it
            title = taskList.listName
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskList = intent.getParcelableExtra(EXTRA_TASK_LIST)!!

        title = taskList.listName

        setContentView(R.layout.activity_task_list)

        val adapter = TaskListAdapter(taskList)

        findViewById<RecyclerView>(R.id.task_list).also {
            it.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            it.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
            it.adapter = adapter
        }

        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            startTaskDetails(this, taskList, null)
        }

        findViewById<FloatingActionButton>(R.id.edit).setOnClickListener {
            taskListDetailLauncher.launch(taskList)
        }

        val viewModel = ViewModelProviders.of(this).get(
            TaskListViewModel::class.java
        )

        lifecycleScope.launchWhenStarted {
            viewModel.tasksFlowOfList(taskList.listId).collect { tasks ->
                adapter.setItems(tasks.map { it.toPO() })
            }
        }
    }

    companion object {
        private const val EXTRA_TASK_LIST = "TaskListActivity.EXTRA_TASK_LIST"

        @JvmStatic
        fun startTaskList(caller: Activity, taskList: TaskListPO) {
            val intent = Intent(caller, TaskListActivity::class.java)
            intent.putExtra(EXTRA_TASK_LIST, taskList)
            caller.startActivity(intent)
        }
    }
}