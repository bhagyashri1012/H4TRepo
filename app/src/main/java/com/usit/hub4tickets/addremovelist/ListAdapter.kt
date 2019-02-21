package com.usit.hub4tickets.addremovelist

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log


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

    var minusClicked:Boolean=false

    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        val multiCitiesForSearch: FlightViewModel.MultiCitiesForSearch =
            FlightViewModel.MultiCitiesForSearch("", "", "")
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
        holder.edtFrom.setOnClickListener {
            minusClicked=false
            listener?.onFromClick(holder.edtTo, holder.edtFrom, position) }
        holder.edtTo.setOnClickListener {
            minusClicked=false
            listener?.onToClick(holder.edtTo, holder.edtFrom, position) }
        holder.edtFrom.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                //Log.d("inTextChange from", s.toString())
                if(!minusClicked) {
                    multiCitiesForSearch.fly_from = s.toString()
                    try {
                        stepList?.set(position, multiCitiesForSearch)
                    } catch (e: Exception) {
                        //multiCitiesForSearch.fly_from = ""
                    }
                }

            }
        })
        holder.edtTo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                //Log.d("inTextChange to", s.toString());
                if(!minusClicked) {
                    multiCitiesForSearch.fly_to = s.toString()
                    try {
                        stepList?.set(position, multiCitiesForSearch)
                    } catch (e: Exception) {
                        //multiCitiesForSearch.fly_to = ""
                    }
                }
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
                try {
                    if(!minusClicked) {
                        multiCitiesForSearch.date_from = s.toString()
                        stepList?.set(position, multiCitiesForSearch)
                    }
                } catch (e:Exception){
                }
            }
        })

        holder.minus.setOnClickListener {
            minusClicked=true
          //  Log.d("steplist - bfr ",stepList.toString())
            if (position in 2..5) {
                try {
                    stepList?.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemChanged(position, itemCount)
                    holder.edtTo.setText(stepList!![position].fly_to)
                    holder.edtFrom.setText(stepList!![position].fly_from)
                    //holder.tvDeparture.text = stepList!![position].date_from
                    //stepList!![position] = multiCitiesForSearch
                } catch (e:IndexOutOfBoundsException) {
                    e.printStackTrace()
                    stepList?.add(FlightViewModel.MultiCitiesForSearch("", "", ""))
                    notifyItemInserted(position + 1)
                    notifyItemChanged(position+1, itemCount)
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
                        //holder.tvDeparture.text = stepList!![position].date_from
                        stepList?.add(FlightViewModel.MultiCitiesForSearch("", "", ""))
                        notifyItemInserted(position)
                        notifyItemChanged(position, itemCount)
                    } else
                        if (stepList?.size == 1 && position == 1) {
                            stepList?.removeAt(position - 1)
                            notifyItemRemoved(position - 1)
                            //stepList!![position].fly_to = ""
                            //stepList!![position].fly_from = ""
                            holder.edtTo.setText(stepList!![position].fly_to)
                            holder.edtFrom.setText(stepList!![position].fly_from)
                            //holder.tvDeparture.text = stepList!![position].date_from
                            stepList?.add(FlightViewModel.MultiCitiesForSearch("", "", ""))
                            notifyItemInserted(position)
                            notifyItemChanged(position, itemCount)
                        } else if (stepList?.size == 2 && position == 2) {
                            stepList?.removeAt(position)
                            notifyItemRemoved(position)
                            //stepList!![position].fly_to = ""
                            //stepList!![position].fly_from = ""
                            holder.edtTo.setText(stepList!![position].fly_to)
                            holder.edtFrom.setText(stepList!![position].fly_from)
                            //holder.tvDeparture.text = stepList!![position].date_from
                            stepList?.add(FlightViewModel.MultiCitiesForSearch("", "", ""))
                            notifyItemInserted(position + 1)
                            notifyItemChanged(position+1, itemCount)
                        } else {

                            stepList!![position].fly_to = ""
                            stepList!![position].fly_from = ""
                            holder.edtTo.setText(stepList!![position].fly_to)
                            holder.edtFrom.setText(stepList!![position].fly_from)
                            //holder.tvDeparture.text = stepList!![position].date_from
                            stepList?.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemChanged(position, itemCount)
                        }
                } catch (e: ArrayIndexOutOfBoundsException) {
                    e.printStackTrace()
                }
                createView(position, holder.plus, holder.minus)
            }
          //  Log.d("step list-aft",stepList.toString())
        }
        stepList!![position].fly_to = holder.edtTo.text.toString()
        stepList!![position].fly_from = holder.edtFrom.text.toString()
        stepList!![position].date_from = holder.tvDeparture.text.toString()
        holder.plus.setOnClickListener {
            minusClicked=false
            if((!holder.edtFrom.text.toString().equals("") && !holder.edtTo.text.toString().equals(""))){
                try {
                    if (stepList!!.size in 1..5) {
                        try {
                            stepList?.add(position + 1, FlightViewModel.MultiCitiesForSearch("", "", ""))
                            //notifyItemInserted(position + 1)
                            notifyItemRangeInserted(position + 1, itemCount)
                        } catch (e: IndexOutOfBoundsException) {
                            e.printStackTrace()
                            stepList?.add(FlightViewModel.MultiCitiesForSearch("", "", ""))
                            notifyItemInserted(position)
                            //notifyItemRangeInserted(position, itemCount)
                        }
                        createView(position, holder.plus, holder.minus)
                    }
                } catch (e: IndexOutOfBoundsException) {
                    e.printStackTrace()
                }
            } else {
                Log.e("position&stepsize", position.toString()+" - "+stepList!!.lastIndex);
                Log.e("ValidationError", "Please enter valid data");
            }
        }
    }

    fun getSearchParamList(): ArrayList<FlightViewModel.MultiCitiesForSearch> {
        var stepListFinal: ArrayList<FlightViewModel.MultiCitiesForSearch>?= ArrayList()
        stepListFinal?.addAll(stepList!!)
        for (list in stepList!!.indices)
        {
            stepListFinal!![list].fly_to=stepListFinal!![list].fly_to.substringAfter("(").substringBefore(")")
            stepListFinal!![list].fly_from=stepListFinal!![list].fly_from.substringAfter("(").substringBefore(")")
        }
        return stepListFinal!!
    }
}