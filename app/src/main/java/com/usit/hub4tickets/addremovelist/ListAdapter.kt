package com.usit.hub4tickets.addremovelist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.usit.hub4tickets.R
import com.usit.hub4tickets.flight.model.FlightViewModel
import com.usit.hub4tickets.utils.Utility


/**
 * Created by Bhagyashri Burade
 * Date: 18/01/2019
 * Email: bhagyashri.burade@usit.net.in
 */
class ListAdapter(
    var stepList: ArrayList<FlightViewModel.MultiCitiesForSearch>?,
    var context: Context,
    val listener: OnItemClickListener?
) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var plus: ImageButton = itemView.findViewById<View>(R.id.plus) as ImageButton
        var minus: ImageButton = itemView.findViewById<View>(R.id.minus) as ImageButton
        var tvDeparture: TextView = itemView.findViewById<View>(R.id.tv_departure) as TextView
        var edtTo: EditText = itemView.findViewById<View>(R.id.edt_to) as EditText
        var edtFrom: EditText = itemView.findViewById<View>(R.id.edt_from) as EditText
    }

    private fun createView(position: Int, plus: ImageButton, minus: ImageButton) {
        if (position == 0) {
            plus.visibility = View.GONE
            minus.visibility = View.VISIBLE
        }
        if (position == itemCount) {
            plus.visibility = View.VISIBLE
            minus.visibility = View.GONE

        }
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return stepList!!.size
    }

    interface OnItemClickListener {
        fun onDepartureDateClick()
        fun onFromClick(edtFrom: EditText, edtFrom1: EditText, position: Int)
        fun onToClick(edtTo: EditText, edtFrom: EditText, position: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListAdapter.ViewHolder {
        val v =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.search_multicity_layout, viewGroup, false)
        return ViewHolder(v)
    }

    var defalutViewHolder: ListAdapter.ViewHolder? = null

//    private val multiCitiesForSearch: FlightViewModel.MultiCitiesForSearch? =
//        FlightViewModel.MultiCitiesForSearch("", "", "")

    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        val multiCitiesForSearch: FlightViewModel.MultiCitiesForSearch =
            FlightViewModel.MultiCitiesForSearch("", "", "")
        defalutViewHolder = holder
        holder.tvDeparture.setOnClickListener { listener?.onDepartureDateClick() }
        holder.edtFrom.setOnClickListener { listener?.onFromClick(holder.edtTo, holder.edtFrom, position) }
        holder.edtTo.setOnClickListener { listener?.onToClick(holder.edtTo, holder.edtFrom, position) }

        if (position == 0)
            holder.plus.visibility = View.GONE
        else
            holder.plus.visibility = View.VISIBLE

        if (position == itemCount - 1)
            holder.minus.visibility = View.GONE
        else {
            holder.plus.visibility = View.GONE
            holder.minus.visibility = View.VISIBLE
        }

        holder.tvDeparture.text = Utility.getCurrentDateNow()

        holder.minus.setOnClickListener {
            try {
                stepList?.removeAt(position)
                notifyItemRemoved(position)
            } catch (e: ArrayIndexOutOfBoundsException) {
                e.printStackTrace()
            }
            if (position == 0 && stepList?.size in 0..1 || position == 1 && stepList?.size in 0..1) {
                stepList?.add(position, FlightViewModel.MultiCitiesForSearch("", "", ""))
                notifyItemInserted(position)
            }
            holder.edtFrom.setText("")
            holder.edtTo.setText("")
            createView(position, holder.plus, holder.minus)
        }

        holder.plus.setOnClickListener {
            try {
                stepList?.add(position + 1, FlightViewModel.MultiCitiesForSearch("", "", ""))
                notifyItemInserted(position + 1)
            } catch (e: ArrayIndexOutOfBoundsException) {
                e.printStackTrace()
            }
            if (position == 0 && stepList?.size in 0..1 || position == 1 && stepList?.size in 0..1) {
                stepList?.add(position + 1, FlightViewModel.MultiCitiesForSearch("", "", ""))
                notifyItemInserted(position + 1)
            }
            createView(position, holder.plus, holder.minus)
        }

        holder.edtFrom.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                multiCitiesForSearch.fly_from = s.toString().substringAfter("(").substringBefore(")")
            }

            override fun afterTextChanged(s: Editable) {}
        })
        holder.edtTo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                multiCitiesForSearch.fly_to = s.toString().substringAfter("(").substringBefore(")")
            }

            override fun afterTextChanged(s: Editable) {}
        })
        multiCitiesForSearch.date_from = holder.tvDeparture.text.toString()
        stepList?.set(position, multiCitiesForSearch)
    }

    fun getSearchParamList(): ArrayList<FlightViewModel.MultiCitiesForSearch> {
        return stepList!!
    }
}
