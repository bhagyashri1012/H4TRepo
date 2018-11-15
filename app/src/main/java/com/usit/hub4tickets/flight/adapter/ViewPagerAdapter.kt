package com.usit.hub4tickets.flight.adapter

import android.content.res.Resources
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.SparseArray
import android.view.ViewGroup
import com.usit.hub4tickets.R
import com.usit.hub4tickets.flight.ui.FragmentMultiCity
import com.usit.hub4tickets.flight.ui.FragmentOneWay
import com.usit.hub4tickets.flight.ui.FragmentReturn

/**
 * Created by Bhagyashri Burade
 * Date: 05/11/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class ViewPagerAdapter(
    resources: Resources,
    fm: FragmentManager
) : FragmentPagerAdapter(fm) {
    private var registeredFragments = SparseArray<Fragment>()

    val resources: Resources = resources
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
            title = resources.getString(R.string.return_flight)
        } else if (position == 1) {
            title = resources.getString(R.string.one_way)
        } else if (position == 2) {
            title = resources.getString(R.string.multicity)
        }
        return title
    }

    /**
     * On each Fragment instantiation we are saving the reference of that Fragment in a Map
     * It will help us to retrieve the Fragment by position
     *
     * @param container
     * @param position
     * @return
     */
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        registeredFragments.put(position, fragment)
        return fragment
    }

    /**
     * Remove the saved reference from our Map on the Fragment destroy
     *
     * @param container
     * @param position
     * @param object
     */
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }


    /**
     * Get the Fragment by position
     *
     * @param position tab position of the fragment
     * @return
     */
    fun getRegisteredFragment(position: Int): Fragment {
        return registeredFragments.get(position)
    }
}
