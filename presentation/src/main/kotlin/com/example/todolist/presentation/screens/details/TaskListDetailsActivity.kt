package com.example.todolist.presentation.screens.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.App.Companion.taskListUseCases
import com.example.todolist.R
import com.example.todolist.presentation.model.TaskListPO
import com.example.todolist.presentation.model.toEntity

class TaskListDetailsActivity : AppCompatActivity() {
    private var taskList: TaskListPO? = null
    private var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_task_list_details)

        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        editText = findViewById(R.id.status_name)

        taskList = intent.getParcelableExtra<TaskListPO>(EXTRA_TASK_LIST)?.also {
            editText?.setText(it.listName)
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
                    taskList = (taskList ?: TaskListPO(0, ""))
                        .copy(listName = "${editText?.text}").apply {
                            taskListUseCases.upsertTaskList(toEntity())
                        }
                    val data = Intent()
                    taskList?.also { data.putExtra(EXTRA_TASK_LIST_RESULT, it) }
                    setResult(RESULT_OK, data)
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val EXTRA_TASK_LIST = "TaskListDetailsActivity.EXTRA_TASK_LIST"
        const val EXTRA_TASK_LIST_RESULT = "TaskListDetailsActivity.EXTRA_TASK_LIST_RESULT"

        @JvmStatic
        fun startTaskListDetails(caller: Activity, taskList: TaskListPO?) {
            caller.startActivity(taskListDetailsIntent(caller, taskList))
        }

        fun taskListDetailsIntent(
            caller: Activity,
            taskList: TaskListPO?
        ): Intent = Intent(caller, TaskListDetailsActivity::class.java).apply {
            taskList?.also { putExtra(EXTRA_TASK_LIST, it) }
        }
    }
}