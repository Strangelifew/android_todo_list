package com.example.todolist.presentation.screens.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.App.Companion.statusUseCases
import com.example.todolist.App.Companion.taskUseCases
import com.example.todolist.R
import com.example.todolist.presentation.model.TaskListPO
import com.example.todolist.presentation.model.TaskPO
import com.example.todolist.presentation.model.toEntity
import com.example.todolist.presentation.model.toPO

class TaskDetailsActivity : AppCompatActivity() {
    private var task: TaskPO? = null
    private var editText: EditText? = null
    private lateinit var taskList: TaskListPO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_task_details)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        title = getString(R.string.task_details_title)

        editText = findViewById(R.id.status_name)

        taskList = intent.getParcelableExtra(EXTRA_TASK_LIST)!!

        task = intent.getParcelableExtra<TaskPO>(EXTRA_TASK)?.also {
            editText?.setText(it.description)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.action_save -> {
                if (!editText?.text.isNullOrEmpty()) {
                    val task = task?.copy(description = "${editText?.text}") ?: TaskPO(
                        0,
                        taskList,
                        statusUseCases.defaultStatus.toPO(),
                        "${editText?.text}"
                    )

                    taskUseCases.upsertTask(task.toEntity())
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val EXTRA_TASK = "TaskDetailsActivity.EXTRA_TASK"
        private const val EXTRA_TASK_LIST = "TaskDetailsActivity.EXTRA_TASK_LIST"

        @JvmStatic
        fun startTaskDetails(caller: Activity, taskList: TaskListPO, task: TaskPO?) {
            Intent(caller, TaskDetailsActivity::class.java).apply {
                putExtra(EXTRA_TASK_LIST, taskList)
                task?.also { putExtra(EXTRA_TASK, it) }
                caller.startActivity(this)
            }
        }
    }
}