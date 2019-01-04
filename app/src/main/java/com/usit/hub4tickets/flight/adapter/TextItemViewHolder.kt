package com.usit.hub4tickets.flight.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.usit.hub4tickets.R
import com.usit.hub4tickets.flight.model.FlightViewModel


class TextItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textView: TextView = itemView.findViewById(R.id.list_item) as TextView
    fun bind(text: String) {
        textView.text = text
    }

}

class TextItemViewHolderForCommonSearch(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textView: TextView = itemView.findViewById(R.id.list_item) as TextView
    val textCountryView: TextView = itemView.findViewById(R.id.list_sub_item) as TextView
    fun bind(text: String, country: String) {
        textView.text = text
        textCountryView.text = country
    }

}

class TextItemViewForTripDetailsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tripTitle: TextView = itemView.findViewById(R.id.trip_title) as TextView
    private val tripDate: TextView = itemView.findViewById(R.id.trip_date) as TextView
    private val tripTime: TextView = itemView.findViewById(R.id.tv_time) as TextView
    private val tripDestinatn: TextView = itemView.findViewById(R.id.tv_destination) as TextView
    private val tripDuration: TextView = itemView.findViewById(R.id.tv_duration) as TextView
    private val tripAirline: TextView = itemView.findViewById(R.id.tv_airline) as TextView
    private val rvStopDetails: RecyclerView = itemView.findViewById(R.id.recycler_view_stop_details) as RecyclerView
    fun bind(tripDetailsResponse: FlightViewModel.TripAllDetails, context: Context?) {
        if (null != tripDetailsResponse) {
            tripTitle.text = tripDetailsResponse?.fromCity + " - " +
                    tripDetailsResponse?.toCity
            tripDate.text = tripDetailsResponse?.startDate
            tripTime.text = tripDetailsResponse?.startTime + " - " +
                    tripDetailsResponse?.endTime
            tripDestinatn.text = tripDetailsResponse?.startAirPortName + " - " +
                    tripDetailsResponse?.endAirPortName
            tripDuration.text = tripDetailsResponse?.duration
            tripAirline.text = tripDetailsResponse?.airline
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvStopDetails!!.layoutManager = layoutManager
            if (null != tripDetailsResponse?.stopDetailsInBound) {
                val adapter = StopDetailsViewAdapter(tripDetailsResponse?.stopDetailsInBound, null)
                rvStopDetails!!.adapter = adapter
            }
            if (null != tripDetailsResponse?.stopDetailsOutBound) {
                val adapter = StopDetailsViewAdapter(null, tripDetailsResponse?.stopDetailsOutBound)
                rvStopDetails!!.adapter = adapter
            }
        }
    }
}

class TextItemViewForStopDetailsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvStopDuration: TextView = itemView.findViewById(R.id.tv_stop_duration) as TextView
    val divider: View = itemView.findViewById(R.id.line) as View
    fun bindStopDetails(
        inBoundStopDetail: FlightViewModel.FlightListResponse.ResponseData.InbondFlightDetails.StopDetail?,
        outBoundStopDetail: FlightViewModel.FlightListResponse.ResponseData.OutbondFlightDetails.StopDetail?
    ) = if (null != inBoundStopDetail) {
        tvStopDuration.text = inBoundStopDetail?.startTime
    } else {
        tvStopDuration.text = outBoundStopDetail?.startTime
    }

    fun lineVisiblity(gone: Int) {
        divider.visibility = gone
    }
}

class TextItemViewHolderForArray(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textView: TextView = itemView.findViewById(R.id.list_item) as TextView
    private val tvTime: TextView = itemView.findViewById(R.id.tv_time) as TextView
    private val tvDestination: TextView = itemView.findViewById(R.id.tv_destination) as TextView
    private val tvDuration: TextView = itemView.findViewById(R.id.tv_duration) as TextView

    private val tvDurationOutBound: TextView = itemView.findViewById(R.id.tv_out_bound_duration) as TextView
    private val tvTimeOutBound: TextView = itemView.findViewById(R.id.tv_out_bound_time) as TextView
    private val tvDestinationOutBound: TextView = itemView.findViewById(R.id.tv_out_bound_destination) as TextView
    private val imvInBound: ImageView = itemView.findViewById(R.id.imv_inbound) as ImageView
    private val imvOutBound: ImageView = itemView.findViewById(R.id.imv_out_bound) as ImageView

    fun bind(responseData: FlightViewModel.FlightListResponse.ResponseData) {
        //out bound
        if (null != responseData.outbondFlightDetails) {
            tvDurationOutBound.text = responseData.outbondFlightDetails?.duration
            tvTimeOutBound.text = responseData.outbondFlightDetails?.startTime + " - " +
                    responseData.outbondFlightDetails?.endTime
            tvDestinationOutBound.text = responseData.outbondFlightDetails?.fromCity + " - " +
                    responseData.outbondFlightDetails?.toCity + " , " +
                    responseData.outbondFlightDetails?.airline
            Glide.with(itemView.context).load(responseData.outbondFlightDetails?.imgUrl).into(imvOutBound)

            textView.text = responseData.currency + " " + responseData.price?.toString()
        }
        if (null != responseData.inbondFlightDetails) {
            tvTime.text = responseData.inbondFlightDetails?.startTime + " - " +
                    responseData.inbondFlightDetails?.endTime
            tvDestination.text = responseData.inbondFlightDetails?.fromCity + " - " +
                    responseData.inbondFlightDetails?.toCity + " , " + responseData.inbondFlightDetails?.airline
            tvDuration.text = responseData.inbondFlightDetails?.duration
            Glide.with(itemView.context).load(responseData.inbondFlightDetails?.imgUrl).into(imvInBound)
        } else {
            tvTime.visibility = View.GONE
            tvDestination.visibility = View.GONE
            tvDuration.visibility = View.GONE
            imvInBound.visibility = View.GONE
        }
    }
}
