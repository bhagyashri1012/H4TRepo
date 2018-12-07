package com.usit.hub4tickets.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.usit.hub4tickets.R
import com.usit.hub4tickets.flight.adapter.TextItemViewHolderForCommonSearch
import com.usit.hub4tickets.search.model.CommonSelectorPojo


/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class CommonSearchAdapter(
    private var strActivityTitle: String?,
    listItems: ArrayList<CommonSelectorPojo>,
    private var listener: OnItemClickListener
) : RecyclerView.Adapter<TextItemViewHolderForCommonSearch>(), Filterable {
    private var arrayListCommonSelector: ArrayList<CommonSelectorPojo> = listItems
    var temArrayListCommonSelector = listItems

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolderForCommonSearch {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_common_serarch_adapter, parent, false)
        return TextItemViewHolderForCommonSearch(view)
    }

    override fun onBindViewHolder(holder: TextItemViewHolderForCommonSearch, position: Int) {
        if (strActivityTitle.equals("FragmentReturn"))
            holder.textCountryView.visibility = View.VISIBLE
        else if (strActivityTitle.equals("FragmentOneWay"))
            holder.textCountryView.visibility = View.VISIBLE
        else
            holder.textCountryView.visibility = View.GONE
        holder.bind(
            temArrayListCommonSelector[position].itemsName.toString(),
            temArrayListCommonSelector[position].code.toString()
        )
        holder.itemView.setOnClickListener { listener.onListItemClick(temArrayListCommonSelector[position]) }
    }

    override fun getItemCount(): Int {
        return if (arrayListCommonSelector != null) temArrayListCommonSelector.size else 0
    }

    var filter: CustomFilter? = null
    override fun getFilter(): Filter {
        if (filter == null) {
            filter = CustomFilter(arrayListCommonSelector, this)
        }

        return filter as CustomFilter
    }


    fun filter(text: String) {
        if (text.isEmpty()) {
            temArrayListCommonSelector.clear()
            temArrayListCommonSelector.addAll(arrayListCommonSelector!!)
        } else {
            temArrayListCommonSelector.clear()
            for (item in arrayListCommonSelector!!) {
                if (item.itemsName?.toLowerCase()?.contains(text)!!) {
                    temArrayListCommonSelector.add(item)
                }
            }
        }
        if (temArrayListCommonSelector.isEmpty()) {
            listener.onNoData(true)
        } else {
            listener.onNoData(false)
        }
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onListItemClick(commonSelectorPojo: CommonSelectorPojo)

        fun onNoData(isVisible: Boolean)
    }
}