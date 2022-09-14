package com.example.todolist.screens.main

import android.app.Activity
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.example.todolist.App
import com.example.todolist.R
import com.example.todolist.model.TaskList
import com.example.todolist.screens.details.TaskListDetailsActivity.Companion.startTaskListDetails
import com.example.todolist.screens.main.MainAdapter.TaskListViewHolder

class MainAdapter : RecyclerView.Adapter<TaskListViewHolder>() {
    private val sortedList: SortedList<TaskList>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        return TaskListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_note_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        holder.bind(sortedList[position])
    }

    override fun getItemCount(): Int {
        return sortedList.size()
    }

    fun setItems(taskLists: List<TaskList>?) {
        sortedList.replaceAll(taskLists!!)
    }

    class TaskListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var noteText: TextView
        var completed: CheckBox
        var delete: View
        lateinit var taskList: TaskList
        var silentUpdate = false
        fun bind(taskList: TaskList) {
            this.taskList = taskList
            noteText.text = taskList.listName
            updateStrokeOut()
            silentUpdate = true
            silentUpdate = false
        }

        private fun updateStrokeOut() {
            noteText.paintFlags = noteText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        init {
            noteText = itemView.findViewById(R.id.note_text)
            completed = itemView.findViewById(R.id.completed)
            delete = itemView.findViewById(R.id.delete)
            itemView.setOnClickListener { startTaskListDetails((itemView.context as Activity), taskList) }
            delete.setOnClickListener { App.instance.taskListDao.delete(taskList) }
        }
    }

    init {
        sortedList = SortedList(TaskList::class.java, object : SortedList.Callback<TaskList>() {
            override fun compare(o1: TaskList, o2: TaskList): Int = o1.listName.compareTo(o2.listName)

            override fun onChanged(position: Int, count: Int) {
                notifyItemRangeChanged(position, count)
            }

            override fun areContentsTheSame(oldItem: TaskList, newItem: TaskList): Boolean {
                return oldItem.equals(newItem)
            }

            override fun areItemsTheSame(item1: TaskList, item2: TaskList): Boolean {
                return item1.listId == item2.listId
            }

            override fun onInserted(position: Int, count: Int) {
                notifyItemRangeInserted(position, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                notifyItemRangeRemoved(position, count)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }
        })
    }
}