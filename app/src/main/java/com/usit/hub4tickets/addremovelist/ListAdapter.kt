package com.usit.hub4tickets.addremovelist

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
import com.usit.hub4tickets.flight.ui.FragmentMultiCity
import com.usit.hub4tickets.utils.Utility
import java.util.*


/**
 * Created by Bhagyashri Burade
 * Date: 18/01/2019
 * Email: bhagyashri.burade@usit.net.in
 */
class ListAdapter(
    var stepList: ArrayList<FlightViewModel.MultiCitiesForSearch>?,
    var context: FragmentMultiCity,
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
            notifyDataSetChanged()
        }
        if (position < itemCount) {
            plus.visibility = View.GONE
            minus.visibility = View.VISIBLE
        }
        if (position == itemCount) {
            plus.visibility = View.VISIBLE
            minus.visibility = View.GONE
            notifyItemChanged(position)
            //notifyDataSetChanged()
        }

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
            if (position in 2..5) {
                try {
                    stepList?.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemChanged(position, itemCount)
                    holder.edtTo.setText(stepList!![position].fly_to)
                    holder.edtFrom.setText(stepList!![position].fly_from)
                    holder.tvDeparture.text = stepList!![position].date_from

                    //stepList!![position] = multiCitiesForSearch

                } catch (e: ArrayIndexOutOfBoundsException) {
                    e.printStackTrace()
                }
                createView(position, holder.plus, holder.minus)
            }
            if (position in 0..1) {
                try {
                    if (stepList?.size == 1 || position == 0) {
                        stepList?.removeAt(position)
                        notifyItemRemoved(position)
                        stepList!![position].fly_to = ""
                        stepList!![position].fly_from = ""
                        holder.edtTo.setText(stepList!![position].fly_to)
                        holder.edtFrom.setText(stepList!![position].fly_from)
                        holder.tvDeparture.text = stepList!![position].date_from
                        stepList?.add(FlightViewModel.MultiCitiesForSearch("", "", ""))
                        notifyItemInserted(position)
                        notifyItemRangeInserted(position, itemCount)
                    } else
                        if (stepList?.size == 1 && position == 1) {
                            stepList?.removeAt(position - 1)
                            notifyItemRemoved(position - 1)
                            stepList!![position].fly_to = ""
                            stepList!![position].fly_from = ""
                            holder.edtTo.setText(stepList!![position].fly_to)
                            holder.edtFrom.setText(stepList!![position].fly_from)
                            holder.tvDeparture.text = stepList!![position].date_from
                            stepList?.add(FlightViewModel.MultiCitiesForSearch("", "", ""))
                            notifyItemInserted(position + 1)
                            notifyItemRangeInserted(position + 1, itemCount)
                        } else if (stepList?.size == 2 && position == 2) {
                            stepList?.removeAt(position)
                            notifyItemRemoved(position)
                            stepList?.add(FlightViewModel.MultiCitiesForSearch("", "", ""))
                            notifyItemInserted(position + 1)
                            notifyItemRangeInserted(position + 1, itemCount)
                        } else {
                            stepList!![position].fly_to = ""
                            stepList!![position].fly_from = ""
                            holder.edtTo.setText(stepList!![position].fly_to)
                            holder.edtFrom.setText(stepList!![position].fly_from)
                            holder.tvDeparture.text = stepList!![position].date_from
                            stepList?.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemChanged(position, itemCount - 1)
                        }
                } catch (e: ArrayIndexOutOfBoundsException) {
                    e.printStackTrace()
                }
                createView(position, holder.plus, holder.minus)
            }

            multiCitiesForSearch.fly_to = stepList!![position].fly_to
            multiCitiesForSearch.fly_from = stepList!![position].fly_from
            multiCitiesForSearch.date_from = stepList!![position].date_from

            stepList!![position].fly_to = ""
            stepList!![position].fly_from = ""
            holder.edtTo.setText("")
            holder.edtFrom.setText("")
        }

        holder.plus.setOnClickListener {
            try {
                if (position in 1..4) {
                    try {
                        stepList?.add(position + 1, FlightViewModel.MultiCitiesForSearch("", "", ""))
                        notifyItemInserted(position + 1)
                        notifyItemRangeInserted(position + 1, itemCount)
                    } catch (e: ArrayIndexOutOfBoundsException) {
                        e.printStackTrace()
                    }
                    createView(position, holder.plus, holder.minus)
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                e.printStackTrace()
            }

        }

        holder.edtFrom.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                multiCitiesForSearch.fly_from = s.toString().substringAfter("(").substringBefore(")")

            }
        })
        holder.edtTo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                multiCitiesForSearch.fly_to = s.toString().substringAfter("(").substringBefore(")")
            }
        })

        multiCitiesForSearch.date_from = holder.tvDeparture.text.toString()

        holder.tvDeparture.setOnClickListener {
            Utility.dateDialogWithMinMaxDate(Calendar.getInstance(), context.activity, holder.tvDeparture, 0)
        }

        holder.tvDeparture.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                multiCitiesForSearch.date_from = s.toString()

            }
        })

        stepList?.set(position, multiCitiesForSearch)

    }

    fun getSearchParamList(): ArrayList<FlightViewModel.MultiCitiesForSearch> {
        return stepList!!
    }
}
