package com.usit.hub4tickets.flight.ui

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import com.savvi.rangedatepicker.CalendarPickerView
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import kotlinx.android.synthetic.main.activity_calender.*
import kotlinx.android.synthetic.main.common_toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class CalenderActivity : BaseActivity() {
    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender)
        init()

        val nextYear = Calendar.getInstance()
        nextYear.add(Calendar.YEAR, 1)

        val lastYear = Calendar.getInstance()
        lastYear.add(Calendar.YEAR, -1)

        /*val list = ArrayList<Int>()
        list.add(12)*/

        //calendar_view.deactivateDates(lastYear)
       /* val arrayList = ArrayList<Date>()
        try {
            val dateformat = SimpleDateFormat("dd-MM-yyyy")
            val strdate = "22-2-2018"
            val strdate2 = "26-2-2018"
            val newdate = dateformat.parse(strdate)
            val newdate2 = dateformat.parse(strdate2)
            arrayList.add(newdate)
            arrayList.add(newdate2)
        } catch (e: Exception) {
            e.printStackTrace()
        }*/

        val arrayList = ArrayList<Date>()
        calendar_view.init(lastYear.time, nextYear.time, SimpleDateFormat("MMMM, YYYY", Locale.getDefault())) //
            .inMode(CalendarPickerView.SelectionMode.RANGE)
            .withSelectedDate(Date())
            //.withDeactivateDates(list)
            .withHighlightedDates(arrayList)
        calendar_view.setTypeface(Typeface.SANS_SERIF)

        get_selected_dates.setOnClickListener(View.OnClickListener {
            Log.d(
                "list",
                calendar_view.selectedDates.toString()

            )
        })
    }

    private fun init() {
        title = resources.getString(R.string.flight_details)
        mainToolbar.setNavigationOnClickListener { onBackPressed() }

    }

}
