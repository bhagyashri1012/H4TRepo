package com.usit.hub4tickets.addremovelist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.usit.hub4tickets.R
import com.usit.hub4tickets.utils.Utility
import java.util.*

/**
 * Created by Bhagyashri Burade
 * Date: 18/01/2019
 * Email: bhagyashri.burade@usit.net.in
 */
class ListAdapter(
    steps: ArrayList<String>,
    internal var context: Context,
    val listener: OnItemClickListener?
) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    var stepList: ArrayList<String>
        internal set

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


    init {
        this.stepList = steps
    }


    override fun getItemCount(): Int {
        return stepList.size
    }

    interface OnItemClickListener {
        fun onDepartureDateClick()
        fun onFromClick(edtFrom: EditText, edtFrom1: EditText)
        fun onToClick(edtTo: EditText, edtFrom: EditText)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListAdapter.ViewHolder {
        val v =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.search_multicity_layout, viewGroup, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        holder.tvDeparture.setOnClickListener { listener?.onDepartureDateClick() }
        holder.edtFrom.setOnClickListener { listener?.onFromClick(holder.edtTo,holder.edtFrom) }
        holder.edtTo.setOnClickListener { listener?.onToClick(holder.edtTo,holder.edtFrom) }

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
                stepList.removeAt(position)
                notifyItemRemoved(position)
            } catch (e: ArrayIndexOutOfBoundsException) {
                e.printStackTrace()
            }
            if (position == 0 && stepList.size in 0..1 || position == 1 && stepList.size in 0..1) {
                stepList.removeAt(position)
                notifyItemRemoved(position)
            }
            holder.edtFrom.setText("")
            holder.edtTo.setText("")
            createView(position, holder.plus, holder.minus)
        }

        holder.plus.setOnClickListener {
            try {
                stepList.add(position + 1, "")
                notifyItemInserted(position + 1)
            } catch (e: ArrayIndexOutOfBoundsException) {
                e.printStackTrace()
            }
            if (position == 0 && stepList.size in 0..1 || position == 1 && stepList.size in 0..1) {
                stepList.add(position + 1, "")
                notifyItemInserted(position + 1)
            }
            createView(position, holder.plus, holder.minus)
        }
    }
}
