package com.usit.hub4tickets.flight.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import com.usit.hub4tickets.flight.adapter.StopDetailsViewAdapter
import com.usit.hub4tickets.flight.model.FlightViewModel
import com.usit.hub4tickets.utils.Constant
import kotlinx.android.synthetic.main.activity_see_details.*
import kotlinx.android.synthetic.main.common_toolbar.*

class SeeDetailsActivity : BaseActivity() {

    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }

    private var stopDetailsResponse: FlightViewModel.TripAllDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_details)
        init()
        if (null != intent.extras) {
            stopDetailsResponse = intent.extras.get(Constant.Path.STOP_DETAILS) as FlightViewModel.TripAllDetails
        }
        setAdapter()
    }

    private val stopDataListAll: ArrayList<FlightViewModel.StopDetail> = ArrayList()

    private fun setAdapter() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view_see_details!!.layoutManager = layoutManager
        if (null != stopDetailsResponse?.stopDetailsInBound) {
            for (i in stopDetailsResponse?.stopDetailsInBound!!.indices) {
                val flightDetails = stopDetailsResponse?.stopDetailsInBound
                if (null != flightDetails) {
                    val tripAllDetails = FlightViewModel.StopDetail(
                        flightDetails[i].airline,
                        flightDetails[i].endAirPortName,
                        flightDetails[i].endDate,
                        flightDetails[i].endTime,
                        flightDetails[i].flightNo,
                        flightDetails[i].imgUrl,
                        flightDetails[i].startAirPortName,
                        flightDetails[i].startDate,
                        flightDetails[i].startTime
                    )
                    stopDataListAll.add(i, tripAllDetails)
                }
            }
        }

        if (null != stopDetailsResponse?.stopDetailsOutBound) {
            for (i in stopDetailsResponse?.stopDetailsOutBound!!.indices) {
                val flightDetails = stopDetailsResponse?.stopDetailsOutBound
                if (null != flightDetails) {
                    val tripAllDetails = FlightViewModel.StopDetail(
                        flightDetails[i].airline,
                        flightDetails[i].endAirPortName,
                        flightDetails[i].endDate,
                        flightDetails[i].endTime,
                        flightDetails[i].flightNo,
                        flightDetails[i].imgUrl,
                        flightDetails[i].startAirPortName,
                        flightDetails[i].startDate,
                        flightDetails[i].startTime
                    )
                    stopDataListAll.add(i, tripAllDetails)
                }
            }
        }

        val adapter = StopDetailsViewAdapter(stopDataListAll)
        recycler_view_see_details!!.adapter = adapter

    }

    private fun init() {
        title = resources.getString(R.string.trip_stop_details)
        mainToolbar.setNavigationOnClickListener { onBackPressed() }
        val layoutManager = LinearLayoutManager(this)
        recycler_view_see_details!!.layoutManager = layoutManager
    }
}
