package com.usit.hub4tickets.flight.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.usit.hub4tickets.R
import com.usit.hub4tickets.flight.model.FlightViewModel

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */

class RecyclerViewAdapter(
    var items: List<FlightViewModel.FlightListResponse.ResponseData>,
    private val listener: OnItemClickListener?
) : RecyclerView.Adapter<TextItemViewHolderForArray>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolderForArray {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_list_item, parent, false)
        return TextItemViewHolderForArray(view)
    }

    override fun onBindViewHolder(holder: TextItemViewHolderForArray, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener { listener?.onFlightRowClick(items[position]) }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    interface OnItemClickListener {
        fun onFlightRowClick(responseData: FlightViewModel.FlightListResponse.ResponseData)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
