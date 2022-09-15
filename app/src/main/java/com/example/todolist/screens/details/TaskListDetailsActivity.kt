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
import com.example.todolist.model.TaskList

class TaskListDetailsActivity : AppCompatActivity() {
    private lateinit var taskList: TaskList
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
        if (intent.hasExtra(EXTRA_TASK_LIST)) {
            taskList = intent.getParcelableExtra(EXTRA_TASK_LIST)!!
            editText?.setText(taskList.listName)
        } else {
            taskList = TaskList()
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
                        App.instance.taskListDao.update(taskList)
                    } else {
                        App.instance.taskListDao.insert(taskList)
                    }
                    finish()

                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val EXTRA_TASK_LIST = "NoteDetailsActivity.EXTRA_TASK_LIST"

        @JvmStatic
        fun startTaskListDetails(caller: Activity, taskList: TaskList?) {
            val intent = Intent(caller, TaskListDetailsActivity::class.java)
            if (taskList != null) {
                intent.putExtra(EXTRA_TASK_LIST, taskList)
            }
            caller.startActivity(intent)
        }
    }
}