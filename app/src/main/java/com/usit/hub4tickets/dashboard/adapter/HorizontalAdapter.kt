package com.usit.hub4tickets.dashboard.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.usit.hub4tickets.R
import com.usit.hub4tickets.dashboard.adapter.HorizontalAdapter.ViewHolder


/**
 * Created by Bhagyashri Burade
 * Date: 30/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class HorizontalAdapter(private val dealImages: IntArray) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_horizontal, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dealPic = dealImages[position]
        //holder.title.text = title
        Glide.with(holder.imvDeals.context).load(dealPic).into(holder.imvDeals)

    }

    override fun getItemCount(): Int {
        return dealImages.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title) as TextView
        val imvDeals: ImageView = itemView.findViewById(R.id.imv_deals) as ImageView

    }
}
