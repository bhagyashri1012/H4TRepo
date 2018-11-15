package com.usit.hub4tickets.flight.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import com.usit.hub4tickets.flight.adapter.TripDetailsViewAdapter
import kotlinx.android.synthetic.main.activity_trip_details.*
import kotlinx.android.synthetic.main.common_toolbar.*

class TripDetailsActivity : BaseActivity() {

    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)
        title = resources.getString(R.string.flight_details)
        mainToolbar.setNavigationOnClickListener { onBackPressed() }

        val items = resources.getStringArray(R.array.return_trip)
        val adapter = TripDetailsViewAdapter(items)
        val layoutManager = LinearLayoutManager(this)
        recycler_view!!.layoutManager = layoutManager
        recycler_view!!.adapter = adapter


        btn_continue_booking.setOnClickListener {
            val intent = Intent(baseContext, TripProvidersListActivity::class.java)
            startActivity(intent)
        }
    }
}
