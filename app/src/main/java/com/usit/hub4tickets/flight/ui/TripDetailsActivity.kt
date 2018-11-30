package com.usit.hub4tickets.flight.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import com.usit.hub4tickets.flight.adapter.TripDetailsViewAdapter
import com.usit.hub4tickets.flight.model.FlightViewModel
import com.usit.hub4tickets.utils.Constant
import kotlinx.android.synthetic.main.activity_trip_details.*
import kotlinx.android.synthetic.main.common_toolbar.*

class TripDetailsActivity : BaseActivity() {

    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)
        init()
        if (null != intent.extras) {
            setDataToRecyclerViewAdapter(intent.extras.getParcelable(Constant.Path.FLIGHT_DETAILS) as FlightViewModel.FlightListResponse.ResponseData)
        }

        btn_continue_booking.setOnClickListener {
            val intent = Intent(baseContext, TripProvidersListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun init() {
        title = resources.getString(R.string.flight_details)
        mainToolbar.setNavigationOnClickListener { onBackPressed() }
        val layoutManager = LinearLayoutManager(this)
        recycler_view!!.layoutManager = layoutManager
    }

    private val dataListAll: ArrayList<FlightViewModel.FlightListResponse.ResponseData>? = ArrayList()
    var adapter: TripDetailsViewAdapter? = TripDetailsViewAdapter(
        items = emptyList(),
        listener = null
    )
    val tripDetailsArrayList: ArrayList<FlightViewModel.TripAllDetails> = ArrayList()

    private fun setDataToRecyclerViewAdapter(responseData: FlightViewModel.FlightListResponse.ResponseData?) {
        if (responseData != null) {
            dataListAll?.add(responseData)
            for (i in dataListAll!!.indices) {
                val flightDetails = dataListAll[i].inbondFlightDetails!!
                val tripAllDetails = FlightViewModel.TripAllDetails(
                    flightDetails.airline,
                    dataListAll[i].inbondFlightDetails!!.currency,
                    dataListAll[i].inbondFlightDetails!!.duration,
                    dataListAll[i].price.toString(),
                    flightDetails.endAirPortName,
                    flightDetails.endDate,
                    flightDetails.endTime,
                    flightDetails.flightNo,
                    flightDetails.fromCity,
                    flightDetails.imgUrl,
                    flightDetails.startAirPortName,
                    flightDetails.startDate,
                    flightDetails.startTime,
                    flightDetails.stopCount,
                    flightDetails.stopDetails,
                    null,
                    flightDetails.toCity
                )
                tripDetailsArrayList.add(i, tripAllDetails)

                val flightDetailsOutBound = dataListAll[i].outbondFlightDetails!!
                val tripAllDetailsOutBound = FlightViewModel.TripAllDetails(
                    flightDetailsOutBound.airline,
                    dataListAll[i].inbondFlightDetails!!.currency,
                    dataListAll[i].inbondFlightDetails!!.duration,
                    dataListAll[i].price.toString(),
                    flightDetailsOutBound.endAirPortName,
                    flightDetailsOutBound.endDate,
                    flightDetailsOutBound.endTime,
                    flightDetailsOutBound.flightNo,
                    flightDetailsOutBound.fromCity,
                    flightDetailsOutBound.imgUrl,
                    flightDetailsOutBound.startAirPortName,
                    flightDetailsOutBound.startDate,
                    flightDetailsOutBound.startTime,
                    flightDetailsOutBound.stopCount,
                    null,
                    flightDetailsOutBound.stopDetails,
                    flightDetailsOutBound.toCity
                )
                tripDetailsArrayList.add(i, tripAllDetailsOutBound)
            }
        }
        adapter = TripDetailsViewAdapter(tripDetailsArrayList, null)
        recycler_view!!.adapter = adapter
    }
}
