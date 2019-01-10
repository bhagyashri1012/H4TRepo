package com.usit.hub4tickets.flight.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.usit.hub4tickets.R
import com.usit.hub4tickets.flight.model.FlightViewModel
import com.usit.hub4tickets.flight.ui.SeeDetailsActivity
import com.usit.hub4tickets.utils.Constant

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
    //private val tripDuration: TextView = itemView.findViewById(R.id.tv_duration) as TextView
    private val tripAirline: TextView = itemView.findViewById(R.id.tv_airline) as TextView
    private val actionSeeDetails: TextView = itemView.findViewById(R.id.action_see_details) as TextView
    private val firstStop: TextView = itemView.findViewById(R.id.txt_from_city) as TextView
    private val lastStop: TextView = itemView.findViewById(R.id.txt_to_city) as TextView
    private val stopDuration: TextView = itemView.findViewById(R.id.tv_stop_duration) as TextView
    private val imageFirstStop: ImageView = itemView.findViewById(R.id.imv_firstStop) as ImageView
    private val imageLastStop: ImageView = itemView.findViewById(R.id.imv_lastStop) as ImageView
    private val stopCount: TextView = itemView.findViewById(R.id.stop_count) as TextView
    //private val rvStopDetails: RecyclerView = itemView.findViewById(R.id.recycler_view_stop_details) as RecyclerView
    fun bind(tripDetailsResponse: FlightViewModel.TripAllDetails, context: Context?) {
        if (null != tripDetailsResponse) {
            tripTitle.text = tripDetailsResponse?.startAirPortName + " - " +
                    tripDetailsResponse?.endAirPortName
            tripDate.text = tripDetailsResponse?.startDate
            tripTime.text = tripDetailsResponse?.startTime + " - " +
                    tripDetailsResponse?.endTime
            tripDestinatn.text = tripDetailsResponse?.fromCity + " - " +
                    tripDetailsResponse?.toCity
            stopCount.text = tripDetailsResponse?.stopCount + " Stop "
            // tripDuration.text = tripDetailsResponse?.duration
            tripAirline.text = tripDetailsResponse.airline
            firstStop.text = tripDetailsResponse.startTime
            lastStop.text = tripDetailsResponse.endTime
            stopDuration.text = tripDetailsResponse.duration

            Glide.with(itemView.context).load(tripDetailsResponse.imgUrl)
                .into(imageFirstStop)
            if (null != tripDetailsResponse.stopDetailsOutBound) {
                Glide.with(itemView.context)
                    .load(tripDetailsResponse.stopDetailsOutBound.get(tripDetailsResponse.stopDetailsOutBound.lastIndex).imgUrl)
                    .into(imageLastStop)
            } else
                if (null != tripDetailsResponse.stopDetailsInBound) {
                    Glide.with(itemView.context)
                        .load(tripDetailsResponse.stopDetailsInBound.get(tripDetailsResponse.stopDetailsInBound.lastIndex).imgUrl)
                        .into(imageLastStop)
                }

            actionSeeDetails.setOnClickListener {
                val intent = Intent(context, SeeDetailsActivity::class.java)
                intent.putExtra(Constant.Path.STOP_DETAILS, tripDetailsResponse)
                context?.startActivity(intent)
            }
        }
    }
}

class TextItemViewForStopDetailsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imvAirline: ImageView = itemView.findViewById(R.id.imv_airline) as ImageView
    private val tripTitle: TextView = itemView.findViewById(R.id.trip_title) as TextView
    private val txtAirline: TextView = itemView.findViewById(R.id.txt_airline) as TextView
    private val tvLocation: TextView = itemView.findViewById(R.id.tv_location) as TextView
    private val tvOutBoundLocation: TextView = itemView.findViewById(R.id.tv_out_bound_location) as TextView
    private val tvDestination: TextView = itemView.findViewById(R.id.tv_destination) as TextView
    private val tvOutBoundDestination: TextView = itemView.findViewById(R.id.tv_out_bound_destination) as TextView
    private val tvDate: TextView = itemView.findViewById(R.id.tv_date) as TextView
    private val tvOutBoundDate: TextView = itemView.findViewById(R.id.tv_out_bound_date) as TextView
    fun bindStopDetails(stopDetail: FlightViewModel.StopDetail?) {
        if (null != stopDetail) {
            //        tvStopDuration.text = outBoundStopDetail?.startTime
            Glide.with(itemView.context)
                .load(stopDetail.imgUrl)
                .into(imvAirline)
            txtAirline.text = "operated by " + stopDetail.airline
            tripTitle.text = "Flight No " +stopDetail.flightNo
            tvDestination.text = stopDetail.startAirPortName + " " + stopDetail.startTime
            tvOutBoundDestination.text = stopDetail.endAirPortName + " " + stopDetail.endTime
            tvDate.text = stopDetail.startTime
            tvOutBoundDate.text = stopDetail.endTime
            tvLocation.text = stopDetail.startAirPortName
            tvOutBoundLocation.text = stopDetail.endAirPortName
        }
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
            tvDestinationOutBound.text = responseData.outbondFlightDetails?.startAirPortName + " - " +
                    responseData.outbondFlightDetails?.endAirPortName + " , " +
                    responseData.outbondFlightDetails?.airline
            Glide.with(itemView.context).load(responseData.outbondFlightDetails?.imgUrl).into(imvOutBound)

            textView.text = responseData.currency + " " + responseData.price?.toString()
        }
        if (null != responseData.inbondFlightDetails) {
            tvTime.text = responseData.inbondFlightDetails?.startTime + " - " +
                    responseData.inbondFlightDetails?.endTime
            tvDestination.text = responseData.inbondFlightDetails?.startAirPortName + " - " +
                    responseData.inbondFlightDetails?.endAirPortName + " , " + responseData.inbondFlightDetails?.airline
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