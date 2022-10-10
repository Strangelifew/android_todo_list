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
import com.example.todolist.App
import com.example.todolist.R
import com.example.todolist.model.Status
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder


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
            ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .initialColor(Color.BLACK)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(20)
                .setPositiveButton(
                    "ok"
                ) { _, selectedColor, _ -> statusColor = Color.valueOf(selectedColor) }
                .setNegativeButton(
                    "cancel"
                ) { _, _ -> }
                .build()
                .show()
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