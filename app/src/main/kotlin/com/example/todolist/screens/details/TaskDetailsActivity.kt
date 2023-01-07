package com.example.todolist.screens.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.data.model.TaskDBO
import com.example.data.model.TaskListDBO
import com.example.todolist.App.Companion.statusUseCases
import com.example.todolist.App.Companion.taskUseCases
import com.example.todolist.App.Companion.toEntity
import com.example.todolist.R

class TaskDetailsActivity : AppCompatActivity() {
    private lateinit var taskDBO: TaskDBO
    private var editText: EditText? = null
    private lateinit var taskList: TaskListDBO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_task_details)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        title = getString(R.string.task_details_title)

        editText = findViewById(R.id.status_name)

        taskList = intent.getParcelableExtra(EXTRA_TASK_LIST)!!

        if (intent.hasExtra(EXTRA_TASK)) {
            taskDBO = intent.getParcelableExtra(EXTRA_TASK)!!
            editText?.setText(taskDBO.description)
        } else {
            taskDBO = TaskDBO(
                listId = taskList.listId,
                statusId = statusUseCases.defaultStatus.statusId
            )
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
                    taskDBO.description = "${editText?.text}"
                    val task = taskDBO.toEntity(
                        taskList.toEntity(),
                        statusUseCases.getStatus(taskDBO.statusId)
                    )
                    if (intent.hasExtra(EXTRA_TASK)) {
                        taskUseCases.updateTask(task)
                    } else {
                        taskUseCases.addTask(task)
                    }
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
        fun startTaskDetails(caller: Activity, taskList: TaskListDBO, task: TaskDBO?) {
            val intent = Intent(caller, TaskDetailsActivity::class.java)
            intent.putExtra(EXTRA_TASK_LIST, taskList)
            if (task != null) {
                intent.putExtra(EXTRA_TASK, task)
            }
            caller.startActivity(intent)
        }
    }
}