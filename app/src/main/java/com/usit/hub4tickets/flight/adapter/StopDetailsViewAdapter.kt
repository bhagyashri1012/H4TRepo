package com.usit.hub4tickets.flight.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.usit.hub4tickets.R
import com.usit.hub4tickets.flight.model.FlightViewModel

/**
 * Created by Bhagyashri Burade
 * Date: 09/01/2019
 * Email: bhagyashri.burade@usit.net.in
 */

class StopDetailsViewAdapter(
    private var stopDetails: ArrayList<FlightViewModel.StopDetail>
) :
    RecyclerView.Adapter<TextItemViewForStopDetailsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewForStopDetailsHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_see_details, parent, false)
        return TextItemViewForStopDetailsHolder(view)
    }

    override fun onBindViewHolder(holder: TextItemViewForStopDetailsHolder, position: Int) {
            holder.bindStopDetails(stopDetails[position])
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return stopDetails.size
    }
}