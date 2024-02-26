package com.chanakya.testview.GraphView


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chanakya.testview.MainActivity
import com.chanakya.testview.R
import com.chanakya.testview.databinding.DistributedLineGraphBinding
import com.chanakya.testview.databinding.ImLineGraphBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler
import com.google.android.material.card.MaterialCardView
import kotlin.math.roundToInt

/**
 * Created by AQUIB RASHID SHAIKH on 23-02-2024.
 *
 * A custom view that extends MaterialCardView to display a line chart using MPAndroidChart library.
 * This custom view simplifies the process of displaying and managing a line chart by encapsulating
 * the chart configuration and data management within a reusable component.
 *
 * @constructor Creates an instance of the DistributedLineGraph custom view.
 * @param context The Context the view is running in, through which it can access the current theme, resources, etc.
 * @param attrs The attributes of the XML tag that is inflating the view.
 * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource to apply to this view.
 *
 */

class IMLineGraph @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    private val binding: ImLineGraphBinding
    private val xAxisValues = ArrayList<String>()

    init {
        binding = ImLineGraphBinding.inflate(LayoutInflater.from(context), this, true)
    }

    /**
     * Invalidates the chart data and refreshes the chart view. This method should be called after
     * data changes or when the chart needs to be redrawn.
     */
    fun invalidateChart() {
        binding.lineChart.apply {
            axisRight.isEnabled = false // Disable right Y-axis
            axisLeft.isEnabled = false // Disable left Y-axis

            animateX(400, Easing.EaseInSine)
            // Only draw labels for X-axis
            // Don't draw X-axis line
            description.isEnabled = false // Disable the description
            legend.isEnabled = false // Disable the legend


            xAxis.apply {
                setDrawGridLines(false)
                setAvoidFirstLastClipping(true)
                setDrawLabels(true)
                setDrawAxisLine(false)
                granularity = 1f
                position = XAxis.XAxisPosition.BOTTOM
                /*axisMinimum = -0.2f
                yOffset = 10f*/
                if (xAxisValues.isNotEmpty()) {
                    valueFormatter = MyAxisFormatter()
                }

            }

            axisLeft.apply {
                // Assuming your dataset's Y values range from 0 to 100
                axisMinimum = 0f - 15f // Adds space below by shifting the minimum Y value down by 5
                axisMaximum = 100f + 15f
                setDrawGridLines(false)// Adds space above by extending the maximum Y value up by 5
            }

// Adds extra padding around the chart, with 10dp on the left
            setExtraOffsets(15f, 0f, 0f, 0f)

        }
    }

    /**
     * Sets the data to be displayed in the line chart. This method accepts a list of lists of Entry objects,
     * where each list represents a line dataset.
     *
     * @param chartData The data to set for the line chart, where each list represents a line dataset.
     */

    fun setDataToLineChart(entries : List<Entry>) {
        // Dummy data

        val lineDataSet = LineDataSet(entries, "Label").apply {
            color = getAssetColor(R.color.shimmer_background_color) // Orange color
            setDrawCircles(true) // Don't draw circles
            setDrawValues(false) // Don't draw values
            setDrawCircleHole(false)
            setCircleColor(getAssetColor(R.color.shimmer_background_color))
            // Set the icon to be drawn at the last point
            val drawableIcon: Drawable? = ContextCompat.getDrawable(context, R.drawable.point)
            setDrawIcons(true)
            //  iconsOffset = MPPointF(0f, -10f)
            lineWidth = 6f
            mode = LineDataSet.Mode.LINEAR // Enable smooth lines
            addEntryOrdered(entries[entries.size-1].apply { icon = drawableIcon })

            setDrawFilled(true) // Enable filling below the line
            fillColor = Color.BLUE // Fallback color
            // Apply the gradient drawable as the fill drawable
            fillDrawable = ContextCompat.getDrawable(context, R.drawable.line_chart_gradient)
        }



        binding.lineChart.data = LineData(lineDataSet)
        binding.lineChart.invalidate() // Refresh the chart

        // Custom MarkerView to display tooltip

        val markerView = CustomMarkerView(context, R.layout.custom_marker_view_layout)
        binding.lineChart.marker = markerView
    }


    class CustomMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {
        private val tvContent: TextView = findViewById(R.id.tvContent)

        // Override this method to set up the tooltip view
        override fun refreshContent(e: Entry?, highlight: Highlight?) {
            tvContent.text = String.format("Value: %s", e?.y.toString())
            super.refreshContent(e, highlight)
        }

        override fun getOffset(): MPPointF {
            return MPPointF(-(width / 2).toFloat(), -height.toFloat())
        }
    }


    inner class MyAxisFormatter : IndexAxisValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String =
            xAxisValues.getOrNull(value.toInt() - 1) ?: ""
    }

    fun setXAxisLabels(labels: ArrayList<String>) {
        xAxisValues.clear()
        xAxisValues.addAll(labels)
    }

    fun getLineChart(): LineChart = binding.lineChart

    private fun getAssetColor(color: Int): Int {
        return ContextCompat.getColor(context, color)
    }


}
