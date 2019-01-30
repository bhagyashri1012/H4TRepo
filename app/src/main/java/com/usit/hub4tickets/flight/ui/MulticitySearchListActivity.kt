package com.usit.hub4tickets.flight.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.usit.hub4tickets.R
import com.usit.hub4tickets.flight.adapter.MultiCityRecyclerViewAdapter
import com.usit.hub4tickets.flight.model.FlightViewModel
import com.usit.hub4tickets.utils.Constant
import com.usit.hub4tickets.utils.Utility
import kotlinx.android.synthetic.main.activity_multicity_search_list.*

class MulticitySearchListActivity : AppCompatActivity(), MultiCityRecyclerViewAdapter.OnItemClickListener {

    private var dataListAll: ArrayList<FlightViewModel.FlightListResponse.ResponseData>? = ArrayList()
    private var totalPassengers: String = ""
    private var adults: String? = "1"
    private var children: String? = "0"
    private var infants: String? = "0"
    var adapter: MultiCityRecyclerViewAdapter? =
        MultiCityRecyclerViewAdapter(
            items = emptyList(),
            listener = null,
            totalPassengers = "",
            price = "",
            currency = ""
        )
    private var travelClass: String? = "Economy"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multicity_search_list)
        if (null != intent.extras) {
            var data: FlightViewModel.ResponseDataMulticity = intent.getParcelableExtra(Constant.Path.MULTICITY_DETAILS)
            if (null != data) {
                dataListAll = data?.multiCityResults as ArrayList<FlightViewModel.FlightListResponse.ResponseData>
                if (null != dataListAll) {
                    setDataToRecyclerViewAdapter(dataListAll, data)
                }
            }
        }

    }

    private fun setDataToRecyclerViewAdapter(
        responseData: ArrayList<FlightViewModel.FlightListResponse.ResponseData>?,
        data: FlightViewModel.ResponseDataMulticity?
    ) {
        dataListAll?.clear()
        if (responseData != null) {
            totalPassengers = Utility.showPassengersAdult(adults).toString() +
                    Utility.showPassengersChildren(children).toString() +
                    Utility.showPassengersInfants(infants).toString()
            dataListAll?.addAll(responseData)
        }
        adapter = MultiCityRecyclerViewAdapter(dataListAll!!, this, totalPassengers, data!!.price.toString(), "")
        recycler_view!!.adapter = adapter
    }

    override fun onFlightRowClick(
        responseData: FlightViewModel.FlightListResponse.ResponseData,
        totalPassengers: String,
        price: String
    ) {
        enterNextFragment(responseData, totalPassengers, price)
    }

    private fun enterNextFragment(
        responseData: FlightViewModel.FlightListResponse.ResponseData,
        totalPassengers: String,
        price: String
    ) {
        val intent = Intent(this, TripDetailsActivity::class.java)
        intent.putExtra(Constant.Path.MULTICITY_DETAILS, responseData)
        intent.putExtra(Constant.Path.PRICE, price)
        intent.putExtra(Constant.Path.TOTAL_PASSENGERS, totalPassengers)
        intent.putExtra(Constant.Path.CABIN_CLASS, travelClass)
        startActivity(intent)
    }
}