package com.example.todolist.presentation.screens.status

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.App.Companion.statusUseCases
import com.example.todolist.R
import com.example.todolist.presentation.model.StatusPO
import com.example.todolist.presentation.screens.details.StatusDetailsActivity.Companion.startStatusDetails
import com.example.todolist.presentation.screens.status.StatusListAdapter.StatusViewHolder

class StatusListAdapter(private val activity: StatusListActivity) :
    RecyclerView.Adapter<StatusViewHolder>() {
    private val statuses = mutableListOf<StatusPO>()


    fun moveItem(from: Int, to: Int) {
        val fromStatus = statuses[from]
        statuses.removeAt(from)
        statuses.add(to, fromStatus)
        statusUseCases.changeStatusOrder(from, to)
        notifyItemMoved(from, to)
    }

    private fun removeItem(status: StatusPO) {
        val i = statuses.indexOf(status)
        statuses.remove(status)
        notifyItemRemoved(i)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {

        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.status_item, parent, false)
        return StatusViewHolder(
            itemView
        ).also { viewHolder ->
            viewHolder.itemView.findViewById<View>(R.id.handleView)
                .setOnTouchListener { _, event ->
                    if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                        activity.startDragging(viewHolder)
                    }
                    true
                }
        }
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.bind(statuses[position])

        holder.itemView.findViewById<View>(R.id.delete).setOnClickListener {
            if (statusUseCases.isEmptyStatus(statuses[position].statusId)) {
                statusUseCases.removeStatus(statuses[position].statusId)
                removeItem(statuses[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return statuses.size
    }

    fun setItems(statuses: List<StatusPO>) {
        this.statuses.clear()
        this.statuses.addAll(statuses)
        notifyItemRangeChanged(0, itemCount)
    }

    class StatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var status: StatusPO
        fun bind(status: StatusPO) {
            this.status = status
            if (!statusUseCases.isEmptyStatus(status.statusId)) {
                itemView.findViewById<View>(R.id.delete).visibility = INVISIBLE
            }
            itemView.findViewById<TextView>(R.id.status_text).apply {
                text = status.statusName
                setTextColor(status.statusColor.toArgb())
            }
        }

        init {
            itemView.setOnClickListener {
                startStatusDetails((itemView.context as Activity), status, status.sortOrder)
            }

            itemView.findViewById<View>(R.id.delete).setOnClickListener {
                if (statusUseCases.isEmptyStatus(status.statusId)) {
                    statusUseCases.removeStatus(status.statusId)
                }
            }
        }
    }
}