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

        binding.mainHorizontalScrollView.overScrollMode = View.OVER_SCROLL_NEVER
        binding.rowTitlesScrollView.overScrollMode = View.OVER_SCROLL_NEVER
        binding.verticalScrollView.overScrollMode = View.OVER_SCROLL_NEVER
        binding.horizontalScrollView.overScrollMode = View.OVER_SCROLL_NEVER
    }

    private fun getDimenInt(id: Int): Int = context.resources.getDimensionPixelSize(id)
    private fun getDimenFloat(id: Int): Float = context.resources.getDimension(id)

    fun createTable(
        tableData: TableData,
        showVerticalDivider: Boolean = true,
        showHorizontalDivider: Boolean = true,
        showTitleDivider:Boolean = false,
        CELL_HEIGHT: Int = getDimenInt(R.dimen.dimen_40dp),
        MIN_CELL_WIDTH: Int = getDimenInt(R.dimen.dimen_122dp),
        MAX_CELL_WIDTH: Int? = null,
        DIVIDER_SIZE: Int = getDimenInt(R.dimen.dimen_1dp),
        displayTopHeader: Boolean = true,
        displayLeftHeader: Boolean = true,
        topHeaderBackGroundColor: Int? = null,
        leftHeaderBackGroundColor: Int? = null,
        rowBackgroundColor: Int? = null,
        topHeaderTextColor: Int? = null,
        leftHeaderTextColor: Int? = null
    ) {
        // Create table rows and cells

        topHeaderBackGroundColor?.let {
            binding.colunmBackground.setBackgroundColor(it)
        }
        leftHeaderBackGroundColor?.let {
            binding.rowTitlesScrollView.setBackgroundColor(it)
        }
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
                //textView.setTextAppearance(R.style.LabelLargeRegular)
                textView.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    CELL_HEIGHT
                )
                textView.minWidth = MIN_CELL_WIDTH
                textView.setPadding(
                    0,
                    getDimenInt(R.dimen.dimen_12dp),
                    0,
                    getDimenInt(R.dimen.dimen_12dp)
                )
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
                    verticalDivider.setBackgroundColor(ContextCompat.getColor(context,R.color.cardBorder))
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
                horizontalDivider.setBackgroundColor(ContextCompat.getColor(context,R.color.cardBorder))
                binding.tableLayout.addView(horizontalDivider)
            }

            // Set background color for odd rows
            if (rowBackgroundColor != null) {
                tableRow.setBackgroundColor(rowBackgroundColor)
            } else if (i % 2 != 0) {
                tableRow.setBackgroundColor(ContextCompat.getColor(context, R.color.cardBorder))
            } else {
                tableRow.setBackgroundColor(ContextCompat.getColor(context, R.color.cardBorder))
            }
        }

        // Create column titles with dividers
        val columnTitleRow = TableRow(context)
        for (i in tableData.columnTitles.indices) {

            if (showTitleDivider && i == 0) {
                val verticalDivider = View(context)
                verticalDivider.layoutParams =
                    TableRow.LayoutParams(DIVIDER_SIZE, TableRow.LayoutParams.MATCH_PARENT)
                verticalDivider.setBackgroundColor(ContextCompat.getColor(context,R.color.cardBorder))
                columnTitleRow.addView(verticalDivider)
            }

            val textView = TextView(context)
            //textView.setTextAppearance(R.style.BodySemibold2)
            textView.layoutParams = TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                CELL_HEIGHT
            )
            textView.minWidth = MIN_CELL_WIDTH
            textView.setPadding(
                0,
                getDimenInt(R.dimen.dimen_12dp),
                0,
                getDimenInt(R.dimen.dimen_12dp)
            )

            if(topHeaderTextColor != null){
                textView.setTextColor(topHeaderTextColor)
            }else {
                textView.setTextColor(ContextCompat.getColor(context,R.color.cardBorder))
            }
            if (MAX_CELL_WIDTH != null) {
                textView.maxWidth = MAX_CELL_WIDTH
            }
            textView.gravity = Gravity.CENTER
            textView.text = tableData.columnTitles[i]

            columnTitleRow.addView(textView)

            // Add vertical divider
            if (showTitleDivider && i < tableData.columnTitles.size - 1) {
                val verticalDivider = View(context)
                verticalDivider.layoutParams =
                    TableRow.LayoutParams(DIVIDER_SIZE, TableRow.LayoutParams.MATCH_PARENT)
                verticalDivider.setBackgroundColor(ContextCompat.getColor(context,R.color.cardBorder))
                columnTitleRow.addView(verticalDivider)
            }
        }
        if (displayTopHeader)
            binding.columnTitlesLayout.addView(columnTitleRow)

        // Add horizontal divider under column titles
        if (showTitleDivider) {
            val horizontalDivider = View(context)
            horizontalDivider.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                DIVIDER_SIZE
            )
            horizontalDivider.setBackgroundColor(ContextCompat.getColor(context,R.color.cardBorder))
            if (displayTopHeader && showTitleDivider)
                binding.columnTitlesLayout.addView(horizontalDivider)
        }

        // Create row titles with dividers
        for (i in tableData.rowTitles.indices) {
            val textView = TextView(context)
          //  textView.setTextAppearance(R.style.BodySemibold2)
            textView.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                CELL_HEIGHT
            )
            textView.minWidth = MIN_CELL_WIDTH
            if(leftHeaderTextColor != null){
                textView.setTextColor(leftHeaderTextColor)
            }else {
          //      textView.setTextColor(ContextCompat.getColor(context,R.color.grey_140))
            }
            textView.setPadding(
                0,
                getDimenInt(R.dimen.dimen_12dp),
                0,
                getDimenInt(R.dimen.dimen_12dp)
            )

            if (MAX_CELL_WIDTH != null) {
                textView.maxWidth = MAX_CELL_WIDTH
            }
            textView.gravity = Gravity.CENTER
            textView.text = tableData.rowTitles[i]

            if (displayLeftHeader)
                binding.rowTitlesLayout.addView(textView)

            // Add horizontal divider
            if (showTitleDivider && i < tableData.rowTitles.size - 1) {
                val horizontalDivider = View(context)
                horizontalDivider.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    DIVIDER_SIZE
                )
              //  horizontalDivider.setBackgroundColor(ContextCompat.getColor(context,R.color.grey_70))
                if (displayLeftHeader && showTitleDivider)
                    binding.rowTitlesLayout.addView(horizontalDivider)
            }
        }
    }

    data class TableData(
        val columnTitles: List<String>,
        val rowTitles: List<String>,
        val tableData: List<List<String>> // Each inner list represents a row of data in the table
        )
}
