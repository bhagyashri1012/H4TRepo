package com.usit.hub4tickets.search

import android.widget.Filter
import com.usit.hub4tickets.search.model.CommonSelectorPojo

/**
 * Created by Bhagyashri Burade
 * Date: 23/11/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class CustomFilter(private var filterList: ArrayList<CommonSelectorPojo>, private var adapter: CommonSearchAdapter) :
    Filter() {

    //FILTERING
    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint

        //RESULTS
        val results = FilterResults()

        //VALIDATION
        if (constraint != null && constraint.isNotEmpty()) {

            //CHANGE TO UPPER FOR CONSISTENCY
            constraint = constraint.toString().toUpperCase()

            val filteredMovies: ArrayList<CommonSelectorPojo> = ArrayList()

            //LOOP THRU FILTER LIST
            for (i in 0 until filterList.size) {
                //FILTER
                if (filterList[i].itemsName?.toUpperCase()?.contains(constraint)!!) {
                    filteredMovies.add(filterList[i])
                }
            }

            results.count = filteredMovies.size
            results.values = filteredMovies
        } else {
            results.count = filterList.size
            results.values = filterList
        }

        return results
    }

    //PUBLISH RESULTS

    override fun publishResults(constraint: CharSequence, results: FilterResults) {
        if (null != results.values)
            adapter.temArrayListCommonSelector = results.values as ArrayList<CommonSelectorPojo>
        adapter.notifyDataSetChanged()

    }
}