package com.example.todolist.screens.status

import android.app.Activity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.App.Companion.statusDao
import com.example.todolist.R
import com.example.todolist.model.Status
import com.example.todolist.screens.details.StatusDetailsActivity.Companion.startStatusDetails
import com.example.todolist.screens.status.StatusListAdapter.StatusViewHolder

class StatusListAdapter(private val activity: StatusListActivity) :
    RecyclerView.Adapter<StatusViewHolder>() {
    private val statuses = mutableListOf<Status>()


    fun moveItem(from: Int, to: Int) {
        val fromStatus = statuses[from]
        statuses.removeAt(from)
        statuses.add(to, fromStatus)
        updateOrder()
        notifyItemMoved(from, to)
    }

    private fun removeItem(status: Status) {
        val i = statuses.indexOf(status)
        statuses.remove(status)
        notifyItemRemoved(i)
    }


    private fun updateOrder() {
        statuses.forEachIndexed { index, status ->
            print("")
            if (status.sortOrder != index) {
                status.sortOrder = index
                statusDao.update(status)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {

        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.status_item, parent, false)
        return StatusViewHolder(
            itemView
        ).also { viewHolder ->
            viewHolder.itemView.findViewById<View>(R.id.handleView)
                .setOnTouchListener { view, event ->
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
            if (statusDao.countNumberOfTasksByStatus(statuses[position].statusId) == 0) {
                statusDao.delete(statuses[position])
                removeItem(statuses[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return statuses.size
    }

    fun setItems(statuses: List<Status>?) {
        this.statuses.clear()
        this.statuses.addAll(statuses!!)
        notifyItemRangeChanged(0, itemCount)
    }

    class StatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var status: Status
        fun bind(status: Status) {
            this.status = status
            if (statusDao.countNumberOfTasksByStatus(status.statusId) != 0) {
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
                if (statusDao.countNumberOfTasksByStatus(status.statusId) == 0) {
                    statusDao.delete(status)
                }
            }
        }
    }

}