package com.usit.hub4tickets.flight.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.usit.hub4tickets.R

/**
 * Created by anupamchugh on 05/10/16.
 */

class RecyclerViewAdapter(
    var items: Array<String>,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<TextItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_list_item, parent, false)
        return TextItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener { listener.onFlightRowClick() }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    interface OnItemClickListener {
        // TODO: Update argument type and name
        fun onFlightRowClick()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
