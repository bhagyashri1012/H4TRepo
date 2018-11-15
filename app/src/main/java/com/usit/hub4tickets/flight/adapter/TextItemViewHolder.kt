package com.usit.hub4tickets.flight.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.usit.hub4tickets.R

class TextItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textView: TextView = itemView.findViewById(R.id.list_item) as TextView


    fun bind(text: String) {
        textView.text = text
    }

}
