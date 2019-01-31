package com.usit.hub4tickets.flight.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.usit.hub4tickets.R
import com.usit.hub4tickets.flight.model.FlightViewModel

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */

class MultiCityRecyclerViewAdapter(
    var items: ArrayList<FlightViewModel.ResponseDataMulticity>?,
    private val listener: MultiCityInnerAdapter.OnItemClickListener?,
    var totalPassengers: String,
    var price: String,
    var currency: String //responseData.currency + " - " + responseData.price?.toString()
) : RecyclerView.Adapter<TextItemViewHolderMulticity>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolderMulticity {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_list_multicity, parent, false)
        return TextItemViewHolderMulticity(view)
    }

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onBindViewHolder(holder: TextItemViewHolderMulticity, position: Int) {
        holder.price.text = items!![position].currencySymbol + " - " + items!![position].price.toString()
        val childLayoutManager = LinearLayoutManager(holder.rc.context, LinearLayout.VERTICAL, false)
        //childLayoutManager.initialPrefetchItemCount = 4
        holder.rc.apply {
            layoutManager = childLayoutManager
            adapter =
                MultiCityInnerAdapter(
                    items!![position].multiCityResults,
                    listener,
                    totalPassengers,
                    items!![position].price.toString(),
                    items!![position].currencySymbol.toString()
                )
            setRecycledViewPool(viewPool)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return items!!.size
    }
}
