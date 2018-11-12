package com.usit.hub4tickets.flight

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.usit.hub4tickets.R
import java.util.*


/**
 * Created by anupamchugh on 05/11/16.
 */


class CustomAdapter(data: ArrayList<*>, private var mContext: Context?) :
    ArrayAdapter<Any>(mContext, R.layout.row_item, data) {

    private val dataSet: ArrayList<String> = data as ArrayList<String>

    // View lookup cache
    private class ViewHolder {
        internal var txtName: TextView? = null

    }

    override fun getItem(position: Int): String? {
        return dataSet[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val viewHolder: ViewHolder // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            convertView = inflater.inflate(R.layout.row_item, parent, false)
            viewHolder.txtName = convertView!!.findViewById(R.id.name) as TextView
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.txtName!!.text = getItem(position)
        // Return the completed view to render on screen
        return convertView
    }
}
