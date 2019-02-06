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
        //var plus: ImageButton = itemView.findViewById<View>(R.id.plus) as ImageButton
        var minus: ImageButton = itemView.findViewById<View>(R.id.minus) as ImageButton
        var tvDeparture: TextView = itemView.findViewById<View>(R.id.tv_departure) as TextView
        var edtTo: EditText = itemView.findViewById<View>(R.id.edt_to) as EditText
        var edtFrom: EditText = itemView.findViewById<View>(R.id.edt_from) as EditText
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

        /* holder.plus.setOnClickListener {
             myClickListener.onAddClick(position, holder)
         }*/
        holder.minus.setOnClickListener {
            myClickListener.onMinusClick(position, holder)
        }
        holder.edtFrom.setOnClickListener {
            myClickListener.onFromClick(holder.edtFrom, holder.edtTo, holder.tvDeparture, position)
            //myClickListener.onEditTextChangeClick(position, holder.edtFrom, "fly_from")
        }
        holder.edtTo.setOnClickListener {
            myClickListener.onToClick(holder.edtTo, holder.edtFrom, holder.tvDeparture, position)
            //myClickListener.onEditTextChangeClick(position, holder.edtTo, "fly_to")
        }
        holder.tvDeparture.setOnClickListener {
            myClickListener.onDepartureDateClick(position, holder.tvDeparture)
            //Utility.dateDialogWithMinMaxDate(Calendar.getInstance(), context.activity, holder.tvDeparture, 0)
        }

        if(mDataset[position].fly_from!="")
        holder.edtFrom.setText(mDataset[position].fly_from.substringBeforeLast("@"))
        if(mDataset[position].fly_to!="")
        holder.edtTo.setText(mDataset[position].fly_to.substringBeforeLast("@"))
        holder.tvDeparture.text = mDataset[position].date_from

    }

    fun addItem(dataObj: FlightViewModel.MultiCitiesForSearch, index: Int) {
        mDataset.add(dataObj)
        Log.d(
            "add  $index",
            mDataset[index].fly_from + " - " + mDataset[index].fly_to + " - " + mDataset[index].date_from
        )
        notifyItemInserted(index)
    }

    fun updateItem(dataObj: FlightViewModel.MultiCitiesForSearch, index: Int) {
        mDataset[index] = dataObj
        Log.d(
            "update  $index",
            mDataset[index].fly_from + " - " + mDataset[index].fly_to + " - " + mDataset[index].date_from
        )
        notifyItemChanged(index)
    }

    fun deleteItem(index: Int) {
        mDataset.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, mDataset.size)
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
        //fun onAddClick(position: Int, v: DataObjectHolder)
        fun onMinusClick(position: Int, v: DataObjectHolder)
        //fun onEditTextChangeClick(position: Int, v: EditText, paramName: String)
        fun onFromClick(edtFrom: EditText, edtTo: EditText, dep: TextView, position: Int)
        fun onToClick(edtTo: EditText, edtFrom: EditText, dep: TextView, position: Int)
        fun onDepartureDateClick(position: Int, v: TextView)
    }

}
