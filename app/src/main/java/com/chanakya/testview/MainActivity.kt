package com.chanakya.testview

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.chanakya.testview.view.DataTableView


class MainActivity : AppCompatActivity() {

    private companion object {
        const val NUM_ROWS = 20
        const val NUM_COLS = 10
    }

    private lateinit var tableData: DataTableView.TableData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize table data
        tableData = DataTableView.TableData(
            columnTitles = List(NUM_COLS) { i -> "C${i + 1}" },
            rowTitles = List(NUM_ROWS) { i -> "R${i + 1}" },
            tableData = List(NUM_ROWS) { i ->
                List(NUM_COLS) { j -> "Data $i$j" }
            }
        )

        val dataTableView = findViewById<DataTableView>(R.id.dataTableView)

        dataTableView.createTable(tableData)

     /*   horizontalScrollView.setOnTouchListener { v, event -> true };
        rowTitlesScrollView.setOnTouchListener { v, event -> true };*/


        // Synchronize horizontal scrolling of column titles and the table

    }


}

