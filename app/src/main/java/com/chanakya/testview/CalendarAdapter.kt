package com.chanakya.testview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.util.Calendar
import java.util.Date

/**
 * Created by AQUIB RASHID SHAIKH on 30-01-2024.
 */
class CalendarAdapter(
    private val context: Context,
    private val dates: ArrayList<Date>,
    /*private val events: HashMap<Date, List<String>>*/) : BaseAdapter() {

    override fun getCount(): Int = dates.size

    override fun getItem(position: Int): Any = dates[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val date = dates[position]
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = convertView ?: inflater.inflate(R.layout.calendar_cell, parent, false)
        val dateText = view.findViewById<TextView>(R.id.calendar_date)
        dateText.text = "${getDayOfMonthFromDate(date)}"

        // Handle displaying events
        /*val eventsForDate = events[date]*/
        // Update the view to show the events

        return view
    }

    fun getDayOfMonthFromDate(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.DAY_OF_MONTH)
    }
}
