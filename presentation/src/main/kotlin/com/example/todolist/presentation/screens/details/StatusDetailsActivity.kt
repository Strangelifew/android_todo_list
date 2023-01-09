package com.example.todolist.presentation.screens.details

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.App.Companion.statusUseCases
import com.example.todolist.R
import com.example.todolist.presentation.model.StatusPO
import com.example.todolist.presentation.model.toEntity
import com.nvt.color.ColorPickerDialog


class StatusDetailsActivity : AppCompatActivity() {
    private var status: StatusPO? = null
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

        status = intent.getParcelableExtra<StatusPO>(EXTRA_STATUS)?.also {
            statusName.setText(it.statusName)
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
                    val status = (status ?: StatusPO(0, 0, "", Color.valueOf(0)))
                        .copy(
                            statusName = "${statusName.text}",
                            sortOrder = intent.getIntExtra(EXTRA_ORDER, -1),
                            statusColor = statusColor
                        )

                    statusUseCases.upsertStatus(status.toEntity())
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
        fun startStatusDetails(caller: Activity, status: StatusPO?, sortOrder: Int) {
            Intent(caller, StatusDetailsActivity::class.java).apply {
                status?.also { putExtra(EXTRA_STATUS, it) }
                putExtra(EXTRA_ORDER, sortOrder)
                caller.startActivity(this)
            }
        }
    }
}