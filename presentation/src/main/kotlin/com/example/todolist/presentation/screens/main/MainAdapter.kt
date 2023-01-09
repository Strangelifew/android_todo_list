package com.example.todolist.presentation.screens.main

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.example.todolist.App.Companion.taskListUseCases
import com.example.todolist.App.Companion.taskUseCases
import com.example.todolist.R
import com.example.todolist.presentation.model.TaskListPO
import com.example.todolist.presentation.screens.main.MainAdapter.TaskListViewHolder
import com.example.todolist.presentation.screens.tasklist.TaskListActivity.Companion.startTaskList

class MainAdapter() :
    RecyclerView.Adapter<TaskListViewHolder>() {
    private val sortedList: SortedList<TaskListPO> =
        SortedList(TaskListPO::class.java, object : SortedList.Callback<TaskListPO>() {
            override fun compare(o1: TaskListPO, o2: TaskListPO): Int =
                o1.listName.compareTo(o2.listName)

            override fun onInserted(position: Int, count: Int) {
                notifyItemRangeInserted(position, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                notifyItemRangeRemoved(position, count)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }

            override fun onChanged(position: Int, count: Int) {
                notifyItemRangeChanged(position, count)
            }

            override fun areContentsTheSame(oldItem: TaskListPO, newItem: TaskListPO): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(item1: TaskListPO, item2: TaskListPO): Boolean =
                item1.listId == item2.listId

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

    fun setItems(taskLists: List<TaskListPO>?) {
        sortedList.replaceAll(taskLists!!)
    }

    class TaskListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val delete: View = itemView.findViewById(R.id.delete)
        private lateinit var taskList: TaskListPO

        fun bind(taskList: TaskListPO) {
            this.taskList = taskList

            itemView.also {
                it.findViewById<TextView>(R.id.task_list_text).text = taskList.listName
                it.findViewById<TextView>(R.id.number_of_tasks).text =
                    "${taskListUseCases.numberOfTasksInList(taskList.listId)}"
            }
        }

        init {
            itemView.setOnClickListener {
                startTaskList((itemView.context as Activity), taskList)
            }

            delete.setOnClickListener {
                fun delete() {
                    taskUseCases.tasksOfList(taskList.listId)
                        .forEach { taskUseCases.removeTask(it.taskId, taskList.listId) }
                    taskListUseCases.removeTaskList(taskList.listId)
                }
                if (taskListUseCases.numberOfTasksInList(taskList.listId) == 0) {
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