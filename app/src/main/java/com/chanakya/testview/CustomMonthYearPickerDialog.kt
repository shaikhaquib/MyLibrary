package com.chanakya.testview

/**
 * Created by AQUIB RASHID SHAIKH on 04-01-2024.
 */
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.NumberPicker
import java.util.*

class CustomMonthYearPickerDialog(context: Context) : Dialog(context) {

    private lateinit var monthPicker: WheelView
    private lateinit var yearPicker: WheelView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_month_year_picker)

        monthPicker = findViewById(R.id.month_picker)
        yearPicker = findViewById(R.id.year_picker)

        val months = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        monthPicker.setItems(months.toMutableList())

        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
       monthPicker.setSeletion(currentMonth)

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = (currentYear - 100..currentYear + 100).toList().map { it.toString() }
        yearPicker.setItems(years)
        yearPicker.setSeletion(years.indexOf(currentYear.toString()))
    }
}
