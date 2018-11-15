package com.usit.hub4tickets.flight.ui


import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.usit.hub4tickets.R
import com.usit.hub4tickets.dashboard.DashboardActivity
import com.usit.hub4tickets.flight.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_flight_main.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class FlightMainFragment : Fragment() {

    private var viewPagerAdapter: ViewPagerAdapter? = null
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.navigation_home -> {
                val intent = Intent(activity?.baseContext, DashboardActivity::class.java)
                intent.putExtra("SCREEN_NAME", "home")
                startActivity(intent)
                activity?.finish()
            }
            R.id.navigation_dashboard -> {
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewPagerAdapter = ViewPagerAdapter(resources, childFragmentManager)
        viewPager.adapter = viewPagerAdapter
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
