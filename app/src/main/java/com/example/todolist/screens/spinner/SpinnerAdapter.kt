package com.example.todolist.screens.spinner

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.todolist.R
import com.example.todolist.model.Status
import kotlin.math.log

class SpinnerAdapter(val context: Context, val statuses: List<Status>) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return statuses.size
    }

    override fun getItem(position: Int): Any {
        return statuses[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ItemHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.status_spiner_item, parent, false)
            viewHolder = ItemHolder(view)
        } else {
            view = convertView
            viewHolder = ItemHolder(view)
        }
        viewHolder.label.text = statuses[position].statusName
        viewHolder.label.setTextColor(statuses[position].statusColor.toArgb())
        return  view
    }

    private class ItemHolder(row: View?) {
        val label: TextView = row?.findViewById(R.id.status_text) as TextView
    }
}