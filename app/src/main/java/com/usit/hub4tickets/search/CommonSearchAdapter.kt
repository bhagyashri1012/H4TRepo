package com.usit.hub4tickets.search

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.usit.hub4tickets.R
import com.usit.hub4tickets.dashboard.DashboardActivity
import com.usit.hub4tickets.search.model.CommonSelectorPojo


/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class CommonSearchAdapter(
    private var mContext: Context,
    listItems: ArrayList<CommonSelectorPojo>,
    private var listener: OnItemClickListener
) : RecyclerView.Adapter<CommonSearchAdapter.ViewHolder>() {


    private var arrayListCommonSelector: ArrayList<CommonSelectorPojo>? = null
    private var temArrayListCommonSelector = ArrayList<CommonSelectorPojo>()

    init {
        arrayListCommonSelector?.addAll(listItems)
        temArrayListCommonSelector.addAll(arrayListCommonSelector!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonSearchAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_common_serarch_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommonSearchAdapter.ViewHolder, position: Int) {

        holder.tvTitle!!.text = temArrayListCommonSelector[position].itemsName

        holder.view.setOnClickListener {
            if (mContext is DashboardActivity) {
                for (index in temArrayListCommonSelector.indices) {
                    temArrayListCommonSelector[index].isSelected = false
                }
                temArrayListCommonSelector[position].isSelected = true
                notifyDataSetChanged()
            }

            listener.onListItemClick(temArrayListCommonSelector[position])
        }

        if (mContext is DashboardActivity) {
            if (temArrayListCommonSelector[position].isSelected) {
                holder.ivTick!!.visibility = View.VISIBLE
            } else {
                holder.ivTick!!.visibility = View.INVISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return if (arrayListCommonSelector != null) temArrayListCommonSelector.size else 0
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
        fun onListItemClick(pinCodeMaster: CommonSelectorPojo)

        fun onNoData(isVisible: Boolean)
    }

    inner class ViewHolder(internal var view: View) : RecyclerView.ViewHolder(view) {

        internal var tvTitle: TextView? = null
        internal var ivTick: ImageView? = null

    }
}
