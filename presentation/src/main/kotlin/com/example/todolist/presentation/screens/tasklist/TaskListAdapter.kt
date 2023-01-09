package com.example.todolist.presentation.screens.tasklist

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.example.todolist.App.Companion.statusUseCases
import com.example.todolist.App.Companion.taskUseCases
import com.example.todolist.R
import com.example.todolist.presentation.model.TaskListPO
import com.example.todolist.presentation.model.TaskPO
import com.example.todolist.presentation.model.toEntity
import com.example.todolist.presentation.model.toPO
import com.example.todolist.presentation.screens.details.TaskDetailsActivity.Companion.startTaskDetails
import com.example.todolist.presentation.screens.spinner.SpinnerAdapter
import com.example.todolist.presentation.screens.tasklist.TaskListAdapter.TaskViewHolder

class TaskListAdapter(private val taskList: TaskListPO) : RecyclerView.Adapter<TaskViewHolder>() {

    private val sortedList: SortedList<TaskPO> =
        SortedList(TaskPO::class.java, object : SortedList.Callback<TaskPO>() {
            override fun compare(o1: TaskPO, o2: TaskPO): Int =
                o1.status.sortOrder.compareTo(o2.status.sortOrder).takeIf { it != 0 }
                    ?: o1.description.compareTo(o2.description)

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

            override fun areContentsTheSame(oldItem: TaskPO?, newItem: TaskPO?): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(item1: TaskPO?, item2: TaskPO?): Boolean = item1 === item2
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

    fun setItems(tasks: List<TaskPO>?) {
        sortedList.replaceAll(tasks!!)
    }

    class TaskViewHolder(itemView: View, private val taskList: TaskListPO) :
        RecyclerView.ViewHolder(itemView) {
        lateinit var task: TaskPO
        fun bind(task: TaskPO) {
            this.task = task

            itemView.findViewById<TextView>(R.id.task_text).text = task.description

            itemView.findViewById<Spinner>(R.id.status).apply {
                val statuses = statusUseCases.allStatuses.value.map { it.toPO() }
                adapter = SpinnerAdapter(itemView.context, statuses)

                setSelection(
                    statuses.withIndex().single {
                        it.value.statusId == statusUseCases.getStatusOrNull(task.status.statusId)?.statusId
                    }.index
                )

                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (task.status.statusId != statuses[position].statusId) {
                            task.status.statusId = statuses[position].statusId
                            taskUseCases.updateTask(task.toEntity())
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
                .setOnClickListener { taskUseCases.removeTask(task.taskId, task.list.listId) }
        }
    }
}