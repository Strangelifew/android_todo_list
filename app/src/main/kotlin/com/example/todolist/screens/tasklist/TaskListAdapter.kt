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
import com.example.data.model.TaskDBO
import com.example.data.model.TaskListDBO
import com.example.todolist.App.Companion.statusUseCases
import com.example.todolist.App.Companion.taskListUseCases
import com.example.todolist.App.Companion.taskUseCases
import com.example.todolist.App.Companion.toDBO
import com.example.todolist.App.Companion.toEntity
import com.example.todolist.R
import com.example.todolist.screens.details.TaskDetailsActivity.Companion.startTaskDetails
import com.example.todolist.screens.spinner.SpinnerAdapter
import com.example.todolist.screens.tasklist.TaskListAdapter.TaskViewHolder

class TaskListAdapter(private val taskList: TaskListDBO) : RecyclerView.Adapter<TaskViewHolder>() {
    private val sortedList: SortedList<TaskDBO> =
        SortedList(TaskDBO::class.java, object : SortedList.Callback<TaskDBO>() {
            val TaskDBO.status get() = statusUseCases.getStatus(statusId)
            override fun compare(o1: TaskDBO, o2: TaskDBO): Int =
                o1.status.sortOrder.compareTo(o2.status.sortOrder).takeIf { it != 0 }
                    ?: o1.description.compareTo(o2.description)

            override fun onChanged(position: Int, count: Int) {
                notifyItemRangeChanged(position, count)
            }

            override fun areContentsTheSame(oldItem: TaskDBO, newItem: TaskDBO): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(item1: TaskDBO, item2: TaskDBO): Boolean {
                return item1 === item2
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

    fun setItems(tasks: List<TaskDBO>?) {
        sortedList.replaceAll(tasks!!)
    }

    class TaskViewHolder(itemView: View, private val taskList: TaskListDBO) :
        RecyclerView.ViewHolder(itemView) {
        lateinit var task: TaskDBO
        fun bind(task: TaskDBO) {
            this.task = task

            itemView.findViewById<TextView>(R.id.task_text).text = task.description

            itemView.findViewById<Spinner>(R.id.status).apply {
                val statuses = statusUseCases.allStatuses.value.map { it.toDBO() }
                adapter = SpinnerAdapter(itemView.context, statuses)

                setSelection(
                    statuses.withIndex().single {
                        it.value.statusId == statusUseCases.getStatus(task.statusId).statusId
                    }.index
                )

                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        print("")
                        if (task.statusId != statuses[position].statusId) {
                            task.statusId = statuses[position].statusId
                            taskUseCases.updateTask(
                                task.toEntity(
                                    taskListUseCases.getTaskListOrNull(task.listId)!!,
                                    statusUseCases.getStatus(task.statusId)
                                )
                            )
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
            }
        }

        init {
            itemView.setOnClickListener {
                startTaskDetails((itemView.context as Activity), taskList, task)
            }

            itemView.findViewById<View>(R.id.delete)
                .setOnClickListener { taskUseCases.removeTask(task.taskId, task.listId) }
        }
    }
}