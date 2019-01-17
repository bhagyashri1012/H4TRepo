package com.usit.hub4tickets.utils

/**
 * Created by Bhagyashri Burade
 * Date: 28/11/2018
 * Email: bhagyashri.burade@usit.net.in
 */

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import com.usit.hub4tickets.R
import com.usit.hub4tickets.search.model.CommonSelectorPojo

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class SignleSelectionAdapter(
    internal var context: Context,
    private val dataList: ArrayList<CommonSelectorPojo>,
    private var listener: OnClickListener
) : RecyclerView.Adapter<SignleSelectionAdapter.MyViewHolder>() {
    private val selectedPosition = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.single_selection_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SignleSelectionAdapter.MyViewHolder, position: Int) {
        val data = dataList[position]
        holder.checkBox.isChecked = dataList[position].isSelected == true
        holder.checkBox.text = dataList[position].itemsName
        holder.checkBox.setOnClickListener {
            for (k in dataList.indices) {
                dataList[k].isSelected = k == position
            }
            notifyDataSetChanged()
            listener.onListItemClick(dataList, position)
        }
    }

    interface OnClickListener {
        fun onListItemClick(commonSelectorPojo: ArrayList<CommonSelectorPojo>, position: Int)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var checkBox: CheckedTextView = itemView.findViewById(R.id.checked_text_item) as CheckedTextView
    }
}