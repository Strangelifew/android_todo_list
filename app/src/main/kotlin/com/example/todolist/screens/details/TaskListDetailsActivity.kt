package com.example.todolist.screens.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.data.model.TaskListDBO
import com.example.todolist.App.Companion.taskListUseCases
import com.example.todolist.App.Companion.toEntity
import com.example.todolist.R

class TaskListDetailsActivity : AppCompatActivity() {
    private lateinit var taskList: TaskListDBO
    private var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_task_list_details)

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        editText = findViewById(R.id.status_name)

        if (intent.hasExtra(EXTRA_TASK_LIST)) {
            taskList = intent.getParcelableExtra(EXTRA_TASK_LIST)!!
            editText?.setText(taskList.listName)
        } else {
            taskList = TaskListDBO()
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
                    taskList.listName = "${editText?.text}"
                    if (intent.hasExtra(EXTRA_TASK_LIST)) {
                        taskListUseCases.updateTaskList(taskList.toEntity())
                    } else {
                        taskListUseCases.addTaskList(taskList.toEntity())
                    }
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val EXTRA_TASK_LIST = "TaskListDetailsActivity.EXTRA_TASK_LIST"

        @JvmStatic
        fun startTaskListDetails(caller: Activity, taskList: TaskListDBO?) {
            val intent = Intent(caller, TaskListDetailsActivity::class.java)
            if (taskList != null) {
                intent.putExtra(EXTRA_TASK_LIST, taskList)
            }
            caller.startActivity(intent)
        }
    }
}