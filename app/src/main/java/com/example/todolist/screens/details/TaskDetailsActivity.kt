package com.example.todolist.screens.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.todolist.App
import com.example.todolist.R
import com.example.todolist.model.Task
import com.example.todolist.model.TaskList
import com.example.todolist.screens.tasklist.TaskListActivity

class TaskDetailsActivity : AppCompatActivity() {
    private lateinit var task: Task
    private var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        title = getString(R.string.note_details_title)
        editText = findViewById(R.id.text)
        if (intent.hasExtra(EXTRA_TASK)) {
            task = intent.getParcelableExtra(EXTRA_TASK)!!
            editText?.setText(task.description)
        } else {
            task = Task(listId = intent.getParcelableExtra<TaskList>(EXTRA_TASK_LIST)!!.listId)
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
                    task.description = "${editText?.text}"
                    if (intent.hasExtra(EXTRA_TASK)) {
                        App.instance.taskDao.update(task)
                    } else {
                        App.instance.taskDao.insert(task)
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
        fun startTaskDetails(caller: Activity, taskList: TaskList, task: Task?) {
            val intent = Intent(caller, TaskDetailsActivity::class.java)
            intent.putExtra(EXTRA_TASK_LIST, taskList)
            if (task != null) {
                intent.putExtra(EXTRA_TASK, task)
            }
            caller.startActivity(intent)
        }
    }
}