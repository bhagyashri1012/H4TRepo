package com.usit.hub4tickets.flight.adapter

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

class TextItemViewForTripDetailsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tripTitle: TextView = itemView.findViewById(R.id.trip_title) as TextView
    private val tripDate: TextView = itemView.findViewById(R.id.trip_date) as TextView
    private val tripTime: TextView = itemView.findViewById(R.id.tv_time) as TextView
    private val tripDestinatn: TextView = itemView.findViewById(R.id.tv_destination) as TextView
    private val tripDuration: TextView = itemView.findViewById(R.id.tv_duration) as TextView
    private val tripAirline: TextView = itemView.findViewById(R.id.tv_airline) as TextView
    fun bind(tripDetailsResponse: FlightViewModel.TripAllDetails) {
        tripTitle.text = tripDetailsResponse?.fromCity + " - " +
                tripDetailsResponse?.toCity
        tripDate.text = tripDetailsResponse?.startDate
        if (null != tripDetailsResponse) {
            tripTime.text = tripDetailsResponse?.startTime + " - " +
                    tripDetailsResponse?.endTime
            tripDestinatn.text = tripDetailsResponse?.startAirPortName + " - " +
                    tripDetailsResponse?.endAirPortName + " ," +
                    tripDetailsResponse?.airline
            tripDuration.text = tripDetailsResponse?.duration
            tripAirline.text = tripDetailsResponse?.airline
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
        if (null != responseData.inbondFlightDetails) {
            tvTime.text = responseData.inbondFlightDetails?.startTime + " - " +
                    responseData.inbondFlightDetails?.endTime
            tvDestination.text = responseData.inbondFlightDetails?.startAirPortName + " - " +
                    responseData.inbondFlightDetails?.endAirPortName + " ," + responseData.inbondFlightDetails?.airline
            tvDuration.text = responseData.inbondFlightDetails?.duration
            Glide.with(itemView.context).load(responseData.inbondFlightDetails?.imgUrl).into(imvInBound)
        } else {
            tvTime.visibility = View.GONE
            tvDestination.visibility = View.GONE
            tvDuration.visibility = View.GONE
            imvInBound.visibility = View.GONE
        }
        //out bound
        tvDurationOutBound.text = responseData.outbondFlightDetails?.duration
        tvTimeOutBound.text = responseData.outbondFlightDetails?.startTime + " - " +
                responseData.outbondFlightDetails?.endTime
        tvDestinationOutBound.text = responseData.outbondFlightDetails?.startAirPortName + " - " +
                responseData.outbondFlightDetails?.endAirPortName + " ," + responseData.outbondFlightDetails?.airline
        Glide.with(itemView.context).load(responseData.outbondFlightDetails?.imgUrl).into(imvOutBound)

        textView.text = responseData.currency.toString() + " " + responseData.price.toString()

    }
}
