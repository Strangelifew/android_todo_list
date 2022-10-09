package com.example.todolist.screens.tasklist

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.example.todolist.App
import com.example.todolist.R
import com.example.todolist.model.Task
import com.example.todolist.model.TaskList
import com.example.todolist.screens.details.TaskDetailsActivity.Companion.startTaskDetails
import com.example.todolist.screens.spinner.SpinnerAdapter
import com.example.todolist.screens.tasklist.TaskListAdapter.TaskViewHolder

class TaskListAdapter(private val taskList: TaskList) : RecyclerView.Adapter<TaskViewHolder>() {
    private val sortedList: SortedList<Task> =
        SortedList(Task::class.java, object : SortedList.Callback<Task>() {
            override fun compare(o1: Task, o2: Task): Int = o1.description.compareTo(o2.description)

            override fun onChanged(position: Int, count: Int) {
                notifyItemRangeChanged(position, count)
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(item1: Task, item2: Task): Boolean {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false),
            taskList
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(sortedList[position])
    }

    override fun getItemCount(): Int {
        return sortedList.size()
    }

    fun setItems(tasks: List<Task>?) {
        sortedList.replaceAll(tasks!!)
    }

    class TaskViewHolder(itemView: View, private val taskList: TaskList) : RecyclerView.ViewHolder(itemView), AdapterView.OnItemSelectedListener {
        lateinit var task: Task
        var statuses = App.statusDao.all
        fun bind(task: Task) {
            this.task = task

            itemView.findViewById<TextView>(R.id.task_text).text = task.description

            itemView.findViewById<Spinner>(R.id.status).also {
                it.adapter = SpinnerAdapter(itemView.context, statuses)

                it.setSelection(App.taskDao.getTaskStatusId(task.taskId))

                it.onItemSelectedListener = this
            }
        }

        init {
            itemView.setOnClickListener {
                startTaskDetails((itemView.context as Activity), taskList, task)
            }

            itemView.findViewById<View>(R.id.delete).setOnClickListener { App.taskDao.delete(task) }




        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            App.taskDao.update(Task(task.taskId, task.listId, position, task.description))
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }

}