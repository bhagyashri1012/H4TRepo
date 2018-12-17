package com.usit.hub4tickets.flight.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.usit.hub4tickets.R
import com.usit.hub4tickets.flight.model.FlightViewModel

/**
 * Created by anupamchugh on 05/10/16.
 */

class TripDetailsViewAdapter(
    private var items: List<FlightViewModel.TripAllDetails>,
    listener: Nothing?,
    private var context: Context
) :
    RecyclerView.Adapter<TextItemViewForTripDetailsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewForTripDetailsHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_trip_details_list_item, parent, false)
        return TextItemViewForTripDetailsHolder(view)
    }

    override fun onBindViewHolder(holder: TextItemViewForTripDetailsHolder, position: Int) {
        holder.bind(items[position], context)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class StopDetailsViewAdapter(
    private var stopDetailsInBound: ArrayList<FlightViewModel.FlightListResponse.ResponseData.InbondFlightDetails.StopDetail>?,
    private var stopDetailsOutBound: List<FlightViewModel.FlightListResponse.ResponseData.OutbondFlightDetails.StopDetail>?
) :
    RecyclerView.Adapter<TextItemViewForStopDetailsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewForStopDetailsHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_stop_details, parent, false)
        return TextItemViewForStopDetailsHolder(view)
    }

    override fun onBindViewHolder(holder: TextItemViewForStopDetailsHolder, position: Int) {
        if (stopDetailsInBound != null) {
            holder.bindStopDetails(stopDetailsInBound?.get(position)!!, null)
            if (position == stopDetailsInBound?.size!!.minus(1))
                holder.lineVisiblity(View.GONE)
        } else {
            holder.bindStopDetails(null, stopDetailsOutBound?.get(position)!!)
            if (position == stopDetailsOutBound?.size!!.minus(1))
                holder.lineVisiblity(View.GONE)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (stopDetailsInBound != null) stopDetailsInBound?.size!! else stopDetailsOutBound?.size!!
    }
}