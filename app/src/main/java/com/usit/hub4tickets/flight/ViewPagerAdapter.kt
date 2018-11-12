package com.usit.hub4tickets.flight

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.usit.hub4tickets.R
/**
 * Created by Bhagyashri Burade
 * Date: 05/11/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class ViewPagerAdapter(
    fm: FragmentManager,
    context: Context
) : FragmentPagerAdapter(fm) {

    val context: Context = context
    override fun getItem(position: Int): Fragment? {
        var fragment: Fragment? = null
        if (position == 0) {
            fragment = FragmentReturn()
        } else if (position == 1) {
            fragment = FragmentOneWay()
        } else if (position == 2) {
            fragment = FragmentMultiCity()
        }
        return fragment
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        if (position == 0) {
            title = context.resources.getString(R.string.return_flight)
        } else if (position == 1) {
            title = context.getString(R.string.one_way)
        } else if (position == 2) {
            title = context.getString(R.string.multicity)
        }
        return title
    }
}
