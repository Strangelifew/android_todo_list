package com.example.todolist.screens.details

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.todolist.App
import com.example.todolist.R
import com.example.todolist.model.Status
import com.example.todolist.screens.status.StatusListViewModel
import com.example.todolist.screens.tasklist.TaskListViewModel
import com.nvt.color.ColorPickerDialog


class StatusDetailsActivity : AppCompatActivity() {
    private var status: Status? = null
    private val statusName: EditText by lazy { findViewById(R.id.status_name) }
    private var statusColor: Color = Color.valueOf(Color.BLACK)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_status_details)

        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)


        findViewById<Button>(R.id.choose_color).setOnClickListener {
            ColorPickerDialog(
                this,
                Color.BLACK,
                true,
                object : ColorPickerDialog.OnColorPickerListener {
                    override fun onCancel(dialog: ColorPickerDialog?) {
                    }

                    override fun onOk(dialog: ColorPickerDialog?, color: Int) {
                        statusColor = Color.valueOf(color)
                    }
                }
            ).show()
        }

        if (intent.hasExtra(EXTRA_STATUS)) {
            status = intent.getParcelableExtra<Status>(EXTRA_STATUS)!!.also {
                statusName.setText(it.statusName)
            }
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
                if (!statusName.text.isNullOrEmpty()) {
                    val sortOrder = intent.getIntExtra(EXTRA_ORDER, -1)
                    val status = status?.also {
                        it.statusName = "${statusName.text}"
                        it.sortOrder = sortOrder
                        it.statusColor = statusColor
                    } ?: Status(
                        sortOrder = sortOrder,
                        statusName = "${statusName.text}",
                        statusColor = statusColor
                    )

                    if (intent.hasExtra(EXTRA_STATUS)) {
                        App.statusDao.update(status)
                    } else {
                        App.statusDao.insert(status)
                    }
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    companion object {
        private const val EXTRA_STATUS = "StatusDetailsActivity.EXTRA_STATUS"
        private const val EXTRA_ORDER = "StatusDetailsActivity.EXTRA_ORDER"

        @JvmStatic
        fun startStatusDetails(caller: Activity, status: Status?, sortOrder: Int) {
            val intent = Intent(caller, StatusDetailsActivity::class.java)
            if (status != null) {
                intent.putExtra(EXTRA_STATUS, status)
            }
            intent.putExtra(EXTRA_ORDER, sortOrder)
            caller.startActivity(intent)
        }
    }
}