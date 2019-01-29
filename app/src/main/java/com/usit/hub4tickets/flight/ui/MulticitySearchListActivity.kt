package com.usit.hub4tickets.flight.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.usit.hub4tickets.R
import com.usit.hub4tickets.flight.adapter.RecyclerViewAdapter
import com.usit.hub4tickets.flight.model.FlightViewModel
import com.usit.hub4tickets.utils.Constant
import com.usit.hub4tickets.utils.Utility
import kotlinx.android.synthetic.main.activity_multicity_search_list.*

class MulticitySearchListActivity : AppCompatActivity(), RecyclerViewAdapter.OnItemClickListener {

    private var dataListAll: ArrayList<FlightViewModel.FlightListResponse.ResponseData>? = ArrayList()
    private var totalPassengers: String = ""
    private var adults: String? = "1"
    private var children: String? = "0"
    private var infants: String? = "0"
    var adapter: RecyclerViewAdapter? =
        RecyclerViewAdapter(
            items = emptyList(),
            listener = null,
            totalPassengers = null,
            className = javaClass.simpleName.toString()
        )
    private var travelClass: String? = "Economy"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multicity_search_list)
        if (null != intent.extras) {
            dataListAll = intent.getParcelableArrayListExtra<FlightViewModel.FlightListResponse.ResponseData>(Constant.Path.MULTICITY_DETAILS)
        }
        if (null != dataListAll) {
            setDataToRecyclerViewAdapter(dataListAll)
        }
    }

    private fun setDataToRecyclerViewAdapter(responseData: ArrayList<FlightViewModel.FlightListResponse.ResponseData>?) {
        dataListAll?.clear()
        if (responseData != null) {
            totalPassengers = Utility.showPassengersAdult(adults).toString() +
                    Utility.showPassengersChildren(children).toString() +
                    Utility.showPassengersInfants(infants).toString()
            dataListAll?.addAll(responseData)
        }
        adapter = RecyclerViewAdapter(dataListAll!!, this, totalPassengers, javaClass.simpleName.toString())
        recycler_view!!.adapter = adapter
    }

    override fun onFlightRowClick(
        responseData: FlightViewModel.FlightListResponse.ResponseData,
        totalPassengers: String?
    ) {
        enterNextFragment(responseData, totalPassengers!!)
    }

    private fun enterNextFragment(
        responseData: FlightViewModel.FlightListResponse.ResponseData,
        totalPassengers: String
    ) {
        val intent = Intent(this, TripDetailsActivity::class.java)
        intent.putExtra(Constant.Path.FLIGHT_DETAILS, responseData)
        intent.putExtra(Constant.Path.PRICE, responseData.price?.toString())
        intent.putExtra(Constant.Path.TOTAL_PASSENGERS, totalPassengers)
        intent.putExtra(Constant.Path.CABIN_CLASS, travelClass)
        startActivity(intent)
    }
}
