package com.usit.hub4tickets.dashboard

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.usit.hub4tickets.R
import com.usit.hub4tickets.dashboard.HorizontalAdapter.ViewHolder


/**
 * Created by Bhagyashri Burade
 * Date: 30/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class HorizontalAdapter(private val titles: Array<String>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_horizontal, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val title = titles[position]
        holder.title.text = title
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView

        init {
            this.title = itemView.findViewById(R.id.title) as TextView
        }
    }
}
