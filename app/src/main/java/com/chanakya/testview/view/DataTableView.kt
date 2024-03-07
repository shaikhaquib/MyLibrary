package com.chanakya.testview.view


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.app.ActivityCompat.recreate
import androidx.core.content.ContextCompat
import com.chanakya.testview.MainActivity
import com.chanakya.testview.R
import com.chanakya.testview.databinding.DistributedLineGraphBinding
import com.chanakya.testview.databinding.TableViewBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler
import com.google.android.material.card.MaterialCardView
import kotlin.math.roundToInt

class DataTableView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    private val binding: TableViewBinding
    private val xAxisValues = ArrayList<String>()

    init {
        binding = TableViewBinding.inflate(LayoutInflater.from(context), this, true)
        initialize()
    }

    fun initialize() {
        binding.mainHorizontalScrollView.setOnScrollChangeListener { _, scrollX, _, _, _ ->
            binding.horizontalScrollView.scrollTo(scrollX, 0)
        }
        binding.horizontalScrollView.setOnScrollChangeListener { _, scrollX, _, _, _ ->
            binding.mainHorizontalScrollView.scrollTo(scrollX, 0)
        }

        // Synchronize vertical scrolling of row titles and the table
        binding.verticalScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            binding.rowTitlesScrollView.scrollTo(0, scrollY)
        }
        binding.rowTitlesScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            binding.verticalScrollView.scrollTo(0, scrollY)
        }
    }

    fun createTable(
        tableData: TableData,
        showVerticalDivider: Boolean = true,
        showHorizontalDivider: Boolean = true,
        CELL_HEIGHT: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
        MIN_CELL_WIDTH: Int = 120,
        MAX_CELL_WIDTH: Int? = null,
        DIVIDER_SIZE: Int = 1,

        ) {
        // Create table rows and cells
        for (i in tableData.rowTitles.indices) {
            val tableRow = TableRow(context)
            tableRow.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )

            for (j in tableData.columnTitles.indices) {
                val cellLayout = LinearLayout(context)
                cellLayout.orientation = LinearLayout.HORIZONTAL

                val textView = TextView(context)
                textView.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    CELL_HEIGHT
                )
                textView.minWidth = MIN_CELL_WIDTH
                if (MAX_CELL_WIDTH != null) {
                    textView.maxWidth = MAX_CELL_WIDTH
                }
                textView.gravity = Gravity.CENTER
                textView.text = tableData.tableData[i][j]

                cellLayout.addView(textView)

                // Add vertical divider
                if (showVerticalDivider && j < tableData.columnTitles.size - 1) {
                    val verticalDivider = View(context)
                    verticalDivider.layoutParams = LinearLayout.LayoutParams(
                        DIVIDER_SIZE,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                    verticalDivider.setBackgroundColor(Color.BLACK)
                    cellLayout.addView(verticalDivider)
                }

                tableRow.addView(cellLayout)
            }

            binding.tableLayout.addView(tableRow)

            // Add horizontal divider
            if (showHorizontalDivider && i < tableData.rowTitles.size - 1) {
                val horizontalDivider = View(context)
                horizontalDivider.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    DIVIDER_SIZE
                )
                horizontalDivider.setBackgroundColor(Color.BLACK)
                binding.tableLayout.addView(horizontalDivider)
            }

            // Set background color for odd rows
            if (i % 2 != 0) {
                tableRow.setBackgroundColor(Color.LTGRAY)
            }
        }

        // Create column titles with dividers
        val columnTitleRow = TableRow(context)
        for (i in tableData.columnTitles.indices) {
            val textView = TextView(context)
            textView.layoutParams = TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                CELL_HEIGHT
            )
            textView.minWidth = MIN_CELL_WIDTH
            if (MAX_CELL_WIDTH != null) {
                textView.maxWidth = MAX_CELL_WIDTH
            }
            textView.gravity = Gravity.CENTER
            textView.text = tableData.columnTitles[i]

            columnTitleRow.addView(textView)

            // Add vertical divider
            if (showVerticalDivider && i < tableData.columnTitles.size - 1) {
                val verticalDivider = View(context)
                verticalDivider.layoutParams =
                    TableRow.LayoutParams(DIVIDER_SIZE, TableRow.LayoutParams.MATCH_PARENT)
                verticalDivider.setBackgroundColor(Color.BLACK)
                columnTitleRow.addView(verticalDivider)
            }
        }
        binding.columnTitlesLayout.addView(columnTitleRow)

        // Add horizontal divider under column titles
        if (showHorizontalDivider) {
            val horizontalDivider = View(context)
            horizontalDivider.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                DIVIDER_SIZE
            )
            horizontalDivider.setBackgroundColor(Color.BLACK)
            binding.columnTitlesLayout.addView(horizontalDivider)
        }

        // Create row titles with dividers
        for (i in tableData.rowTitles.indices) {
            val textView = TextView(context)
            textView.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                CELL_HEIGHT
            )
            textView.minWidth = MIN_CELL_WIDTH
            if (MAX_CELL_WIDTH != null) {
                textView.maxWidth = MAX_CELL_WIDTH
            }
            textView.gravity = Gravity.CENTER
            textView.text = tableData.rowTitles[i]

            binding.rowTitlesLayout.addView(textView)

            // Add horizontal divider
            if (showHorizontalDivider && i < tableData.rowTitles.size - 1) {
                val horizontalDivider = View(context)
                horizontalDivider.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    DIVIDER_SIZE
                )
                horizontalDivider.setBackgroundColor(Color.BLACK)
                binding.rowTitlesLayout.addView(horizontalDivider)
            }
        }
    }

    data class TableData(
        val columnTitles: List<String>,
        val rowTitles: List<String>,
        val tableData: List<List<String>> // Each inner list represents a row of data in the table
    )
}
