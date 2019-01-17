package com.usit.hub4tickets.flight.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import com.usit.hub4tickets.flight.adapter.TripDetailsViewAdapter
import com.usit.hub4tickets.flight.model.FlightViewModel
import com.usit.hub4tickets.utils.Constant
import com.usit.hub4tickets.utils.webview.WebViewActivity
import kotlinx.android.synthetic.main.activity_trip_details.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlin.math.roundToInt

class TripDetailsActivity : BaseActivity() {

    private val tripDetailsArrayList: ArrayList<FlightViewModel.TripAllDetails> = ArrayList()
    private val dataListAll: ArrayList<FlightViewModel.FlightListResponse.ResponseData>? = ArrayList()
    var adapter: TripDetailsViewAdapter? = TripDetailsViewAdapter(
        items = emptyList(),
        listener = null,
        context = this
    )
    private var response: FlightViewModel.FlightListResponse.ResponseData? = null
    private var deeplink = ""
    private var dataProvider = ""

    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)
        init()
        if (null != intent.extras) {
            response =
                    intent.extras.getParcelable(Constant.Path.FLIGHT_DETAILS) as FlightViewModel.FlightListResponse.ResponseData
            setDataToRecyclerViewAdapter(response)
            tv_details_passengers.text = intent.extras.getString(Constant.Path.TOTAL_PASSENGERS)
            tv_details_class.text = intent.extras.getString(Constant.Path.CABIN_CLASS)
            tv_details_price.text = (intent.extras.getParcelable(Constant.Path.FLIGHT_DETAILS) as FlightViewModel.FlightListResponse.ResponseData).currency +
                    " " +
                    intent.extras.getString(Constant.Path.PRICE)
            tv_total_deals.text = "1 Deal from " + response?.currency + " " + intent.extras.getString(Constant.Path.PRICE)
        }
        btn_continue_booking.setOnClickListener {
            // val intent = Intent(baseContext, TripProvidersListActivity::class.java)
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra(Constant.Path.URL, deeplink)
            intent.putExtra(Constant.Path.HEADING, dataProvider)
            startActivity(intent)
        }
    }

    private fun init() {
        title = resources.getString(R.string.flight_details)
        mainToolbar.setNavigationOnClickListener { onBackPressed() }
        val layoutManager = LinearLayoutManager(this)
        recycler_view!!.layoutManager = layoutManager
    }

    private fun setDataToRecyclerViewAdapter(responseData: FlightViewModel.FlightListResponse.ResponseData?) {
        if (responseData != null) {
            dataListAll?.add(responseData)
            deeplink = responseData.deepLink.toString()
            dataProvider = responseData.dataProvider.toString()
            for (i in dataListAll!!.indices) {
                val flightDetails = dataListAll[i].inbondFlightDetails
                if (null != flightDetails) {
                    val tripAllDetails = FlightViewModel.TripAllDetails(
                        flightDetails?.airline,
                        flightDetails.currency,
                        flightDetails.duration,
                        dataListAll[i].price.toString(),
                        flightDetails.endAirPortName,
                        flightDetails?.endDate,
                        flightDetails?.endTime,
                        flightDetails?.flightNo,
                        flightDetails?.fromCity,
                        flightDetails?.imgUrl,
                        flightDetails?.startAirPortName,
                        flightDetails?.startDate,
                        flightDetails?.startTime,
                        flightDetails?.stopCount?.toString(),
                        flightDetails?.stopDetails,
                        null,
                        flightDetails?.toCity
                    )
                    tripDetailsArrayList.add(i, tripAllDetails)
                }

                val flightDetailsOutBound = dataListAll[i].outbondFlightDetails
                if (null != flightDetailsOutBound) {
                    val tripAllDetailsOutBound = FlightViewModel.TripAllDetails(
                        flightDetailsOutBound?.airline,
                        flightDetailsOutBound.currency,
                        flightDetailsOutBound.duration,
                        dataListAll[i].price?.roundToInt().toString(),
                        flightDetailsOutBound?.endAirPortName,
                        flightDetailsOutBound?.endDate,
                        flightDetailsOutBound?.endTime,
                        flightDetailsOutBound?.flightNo,
                        flightDetailsOutBound?.fromCity,
                        flightDetailsOutBound?.imgUrl,
                        flightDetailsOutBound?.startAirPortName,
                        flightDetailsOutBound?.startDate,
                        flightDetailsOutBound?.startTime,
                        flightDetailsOutBound?.stopCount?.toString(),
                        null,
                        flightDetailsOutBound?.stopDetails,
                        flightDetailsOutBound?.toCity
                    )
                    tripDetailsArrayList.add(i, tripAllDetailsOutBound)
                }
            }
        }
        adapter = TripDetailsViewAdapter(tripDetailsArrayList, null, this)
        recycler_view!!.adapter = adapter
    }
}

