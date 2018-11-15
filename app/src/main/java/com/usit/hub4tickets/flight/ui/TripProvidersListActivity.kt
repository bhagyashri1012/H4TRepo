package com.usit.hub4tickets.flight.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import com.usit.hub4tickets.flight.adapter.TripProvidersAdapter
import kotlinx.android.synthetic.main.activity_trip_providers_list.*
import kotlinx.android.synthetic.main.common_toolbar.*

class TripProvidersListActivity : BaseActivity() {
    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_providers_list)

        title = resources.getString(R.string.select_provider)
        mainToolbar.setNavigationOnClickListener { onBackPressed() }

        val items = resources.getStringArray(R.array.return_trip)
        val adapter = TripProvidersAdapter(items)
        val layoutManager = LinearLayoutManager(this)
        recycler_view!!.layoutManager = layoutManager
        recycler_view!!.adapter = adapter
    }
}
