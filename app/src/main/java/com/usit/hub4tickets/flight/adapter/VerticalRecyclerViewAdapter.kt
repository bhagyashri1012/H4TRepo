package com.usit.hub4tickets.flight.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.usit.hub4tickets.R
import com.usit.hub4tickets.flight.model.FlightViewModel
import java.util.*


class VerticalRecyclerViewAdapter(
    private val mDataset: ArrayList<FlightViewModel.MultiCitiesForSearch>,
    private val myClickListener: VerticalRecyclerViewAdapter.MyClickListener
) :
    RecyclerView.Adapter<VerticalRecyclerViewAdapter.DataObjectHolder>() {
    class DataObjectHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var minus: ImageButton = itemView.findViewById<View>(R.id.minus) as ImageButton
        var tvDeparture: TextView = itemView.findViewById<View>(R.id.tv_departure) as TextView
        var edtTo: EditText = itemView.findViewById<View>(R.id.edt_to_multicity) as EditText
        var edtFrom: EditText = itemView.findViewById<View>(R.id.edt_from_multicity) as EditText
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VerticalRecyclerViewAdapter.DataObjectHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_multicity_layout, parent, false)
        return DataObjectHolder(view)
    }

    override fun onBindViewHolder(holder: VerticalRecyclerViewAdapter.DataObjectHolder, position: Int) {

        holder.minus.setOnClickListener {
            myClickListener.onMinusClick(position, holder)
        }
        holder.edtFrom.setOnClickListener {
            myClickListener.onFromClick(holder.edtFrom, holder.edtTo, holder.tvDeparture, position)
        }
        holder.edtTo.setOnClickListener {
            myClickListener.onToClick(holder.edtTo, holder.edtFrom, holder.tvDeparture, position)
        }
        holder.tvDeparture.setOnClickListener {
            myClickListener.onDepartureDateClick(position, holder.tvDeparture)
        }
        if (mDataset[position].fly_from.isNotBlank())
            holder.edtFrom.setText(mDataset[position].fly_from.substringBeforeLast("@"))
        else
            holder.edtFrom.setText("")
        if (mDataset[position].fly_to.isNotBlank())
            holder.edtTo.setText(mDataset[position].fly_to.substringBeforeLast("@"))
        else
            holder.edtTo.setText("")
        holder.tvDeparture.text = mDataset[position].date_from
    }

    fun addItem(dataObj: FlightViewModel.MultiCitiesForSearch, index: Int) {
        try {
            mDataset.add(dataObj)
            Log.d(
                "add  $index",
                mDataset[index].fly_from + " - " + mDataset[index].fly_to + " - " + mDataset[index].date_from
            )
            notifyItemInserted(index)
        } catch (e: Exception) {
            e.message
        }
    }

    fun updateItem(dataObj: FlightViewModel.MultiCitiesForSearch, index: Int) {
        try {
            mDataset[index] = dataObj
            Log.d(
                "update  $index",
                mDataset[index].fly_from + " - " + mDataset[index].fly_to + " - " + mDataset[index].date_from
            )
            notifyItemChanged(index)
        } catch (e: Exception) {
            e.message
        }
    }

    fun deleteItem(index: Int) {
        try {
            mDataset.removeAt(index)
            notifyItemRemoved(index)
            notifyItemRangeChanged(index, mDataset.size)
        } catch (e: Exception) {
            e.message
        }
    }

    fun getSearchParamList(): ArrayList<FlightViewModel.MultiCitiesForSearch> {
        var stepListFinal: ArrayList<FlightViewModel.MultiCitiesForSearch>? = ArrayList()
        stepListFinal?.addAll(mDataset)
        return stepListFinal!!
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    interface MyClickListener {
        fun onMinusClick(position: Int, v: DataObjectHolder)
        fun onFromClick(edtFrom: EditText, edtTo: EditText, dep: TextView, position: Int)
        fun onToClick(edtTo: EditText, edtFrom: EditText, dep: TextView, position: Int)
        fun onDepartureDateClick(position: Int, v: TextView)
    }
}