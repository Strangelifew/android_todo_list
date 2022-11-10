package com.example.todolist.screens.main

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.example.todolist.App
import com.example.todolist.R
import com.example.todolist.model.Status
import com.example.todolist.model.TaskList
import com.example.todolist.screens.main.MainAdapter.TaskListViewHolder
import com.example.todolist.screens.tasklist.TaskListActivity.Companion.startTaskList

class MainAdapter(private val mainActivity: MainActivity) :
    RecyclerView.Adapter<TaskListViewHolder>() {
    private val sortedList: SortedList<TaskList> =
        SortedList(TaskList::class.java, object : SortedList.Callback<TaskList>() {
            override fun compare(o1: TaskList, o2: TaskList): Int =
                o1.listName.compareTo(o2.listName)

            override fun onChanged(position: Int, count: Int) {
                notifyItemRangeChanged(position, count)
            }

            override fun areContentsTheSame(oldItem: TaskList, newItem: TaskList): Boolean {
                return oldItem == newItem
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        return TaskListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_list_item, parent, false)
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
        private val delete: View = itemView.findViewById(R.id.delete)
        private lateinit var taskList: TaskList

        fun bind(taskList: TaskList) {
            this.taskList = taskList

            itemView.also {
                it.findViewById<TextView>(R.id.task_list_text).text = taskList.listName
                it.findViewById<TextView>(R.id.number_of_tasks).text =
                    App.taskListDao.countNumberOfTasksInList(this.taskList.listId).toString()
            }
        }

        init {
            itemView.setOnClickListener {
                startTaskList((itemView.context as Activity), taskList)
            }

            delete.setOnClickListener {
                fun delete() {
                    App.taskDao.findByListId(taskList.listId).value?.forEach(App.taskDao::delete)
                    App.taskListDao.delete(taskList)
                }
                if (App.taskListDao.countNumberOfTasksInList(taskList.listId) == 0) {
                    delete()
                } else {
                    AlertDialog.Builder(delete.context)
                        .setMessage("Are you sure to delete non empty task list?")
                        .setPositiveButton("YES") { _, _ ->
                            delete()
                        }
                        .setNegativeButton("NO") { _, _ -> }
                        .show()
                }
            }
        }
    }

}