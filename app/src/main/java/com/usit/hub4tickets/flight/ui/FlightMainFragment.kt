package com.usit.hub4tickets.flight.ui


import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.usit.hub4tickets.R
import com.usit.hub4tickets.dashboard.ui.DashboardActivity
import com.usit.hub4tickets.flight.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_flight_main.*

class FlightMainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    private var viewPagerAdapter: ViewPagerAdapter? = null
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.navigation_home -> {
                val intent = Intent(activity?.baseContext, DashboardActivity::class.java)
                intent.putExtra("SCREEN_NAME", "home")
                startActivity(intent)
                activity?.finish()
            }
            R.id.navigation_my_account -> {
                val intent = Intent(activity?.baseContext, DashboardActivity::class.java)
                intent.putExtra("SCREEN_NAME", "account")
                startActivity(intent)
                activity?.finish()
            }
            R.id.navigation_help -> {
                val intent = Intent(activity?.baseContext, DashboardActivity::class.java)
                intent.putExtra("SCREEN_NAME", "help")
                startActivity(intent)
                activity?.finish()
            }
        }
        return@OnNavigationItemSelectedListener true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flight_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPagerAdapter = ViewPagerAdapter(resources, childFragmentManager)
        viewPager.adapter = viewPagerAdapter
        viewPager.offscreenPageLimit = 0
        tabs.setupWithViewPager(viewPager)
        navigation_flight.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    /**
     * Retrieve the currently visible Tab Fragment and propagate the onBackPressed callback
     *
     * @return true = if this fragment and/or one of its associates Fragment can handle the backPress
     */
    fun onBackPressed(): Boolean {
        // currently visible tab Fragment
        val currentFragment = viewPagerAdapter?.getRegisteredFragment(viewPager.currentItem) as OnBackPressListener
        return currentFragment?.onBackPressed() ?: false
        // this Fragment couldn't handle the onBackPressed call
    }

}
