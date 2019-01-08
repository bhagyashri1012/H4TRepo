package com.usit.hub4tickets.dashboard.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView
import com.usit.hub4tickets.R
import com.usit.hub4tickets.dashboard.adapter.HorizontalAdapter
import com.usit.hub4tickets.dashboard.ui.settings.SettingsFragment
import com.usit.hub4tickets.flight.ui.MainFlightActivity
import com.usit.hub4tickets.flight.ui.RootFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : RootFragment() {


    private val hotDealsImagesArray =
        intArrayOf(R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isResumed) {
            val firstAdapter = HorizontalAdapter(hotDealsImagesArray)
            val firstRecyclerView = view.findViewById(R.id.recycler_view) as MultiSnapRecyclerView
            val firstManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            firstRecyclerView.layoutManager = firstManager
            firstRecyclerView.adapter = firstAdapter
        }
        imv_settings.setOnClickListener {

            val transaction = fragmentManager?.beginTransaction()
            transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.slide_to_left)
            transaction?.replace(R.id.frame_layout_settings, SettingsFragment.newInstance()!!)
            transaction?.commit()
        }

        ll_flight.setOnClickListener {
            val intent = Intent(context, MainFlightActivity::class.java)
            startActivity(intent)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

}
