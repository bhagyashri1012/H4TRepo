package com.usit.hub4tickets.flight.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.usit.hub4tickets.R
import com.usit.hub4tickets.flight.model.FlightViewModel
import com.usit.hub4tickets.flight.ui.FragmentMultiCity


class VerticalRecyclerViewAdapter(
    private val mDataset: ArrayList<FlightViewModel.MultiCitiesForSearch>,
    private val myClickListener: VerticalRecyclerViewAdapter.MyClickListener) :
    RecyclerView.Adapter<VerticalRecyclerViewAdapter.DataObjectHolder>() {

    class DataObjectHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var plus: ImageButton = itemView.findViewById<View>(R.id.plus) as ImageButton
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
        holder.edtFrom.setText(mDataset[position].fly_from)
        holder.edtTo.setText(mDataset[position].fly_to)
        holder.plus.setOnClickListener { myClickListener.onAddClick(position,holder.minus)}
        holder.minus.setOnClickListener { myClickListener.onMinusClick(position,holder.minus)}

    }

     fun addItem(dataObj: FlightViewModel.MultiCitiesForSearch, index: Int) {
        mDataset.add(dataObj)
        notifyItemInserted(index)
    }

     fun deleteItem(index: Int) {
        mDataset.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }


    public interface MyClickListener {
        fun onAddClick(position: Int, v: View)
        fun onMinusClick(position: Int, v: View)
    }

}
