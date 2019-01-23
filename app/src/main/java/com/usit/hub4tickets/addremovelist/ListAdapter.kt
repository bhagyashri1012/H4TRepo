package com.usit.hub4tickets.addremovelist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.usit.hub4tickets.R
import java.util.*

/**
 * Created by Bhagyashri Burade
 * Date: 18/01/2019
 * Email: bhagyashri.burade@usit.net.in
 */
class ListAdapter(steps: ArrayList<String>, internal var context: Context) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    var stepList: ArrayList<String>
        internal set

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var plus: ImageButton = itemView.findViewById<View>(R.id.plus) as ImageButton
        var minus: ImageButton = itemView.findViewById<View>(R.id.minus) as ImageButton

        init {
            if (position == 0)
                minus.visibility = View.GONE
            else
                minus.visibility = View.VISIBLE
            minus.setOnClickListener {
                val position = adapterPosition

                try {
                    stepList.removeAt(position)
                    notifyItemRemoved(position)
                } catch (e: ArrayIndexOutOfBoundsException) {
                    e.printStackTrace()
                }

                if (position == 0 && stepList.size in 0..1 || position == 1 && stepList.size in 0..1) {
                    stepList.add(position, "")
                    notifyItemInserted(position)
                }
            }

            plus.setOnClickListener {
                val position = adapterPosition
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
            }

            /* main_multicity_layout.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    steps.set(getAdapterPosition(), s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });*/
        }
    }


    init {
        this.stepList = steps
    }


    override fun getItemCount(): Int {
        return stepList.size
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListAdapter.ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.search_multicity_layout, viewGroup, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, i: Int) {

        val position = holder.layoutPosition

    }
}
