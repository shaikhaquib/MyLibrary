package com.chanakya.testview.GraphView


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.chanakya.testview.R
import com.chanakya.testview.databinding.DistributedLineGraphBinding
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

class DistributedLineGraph @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    private val binding: DistributedLineGraphBinding
    private val xAxisValues = ArrayList<String>()

    init {
        binding = DistributedLineGraphBinding.inflate(LayoutInflater.from(context), this, true)
    }

    /**
     * Invalidates the chart data and refreshes the chart view. This method should be called after
     * data changes or when the chart needs to be redrawn.
     */
    fun invalidateChart() {
        with(binding.lineChart) {
            isDoubleTapToZoomEnabled = true
            resetZoom()
            zoom(1.5f, 1.5f, 1f, 0f, YAxis.AxisDependency.RIGHT)
            animateX(400, Easing.EaseInSine)
            legend.isEnabled = false
            description.isEnabled = false
            isScaleXEnabled = true
            setTouchEnabled(true)
            isDragEnabled = true
            xAxis.apply {
                setAvoidFirstLastClipping(true)
                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1F
                if (xAxisValues.isNotEmpty()) {
                    valueFormatter = MyAxisFormatter()
                }
                setDrawAxisLine(true)
                axisMinimum = -0.2f
                yOffset = 10f
            }
            axisLeft.apply {
                valueFormatter = ClaimsYAxisValueFormatter()
                granularity = 1F
                enableGridDashedLine(10f, 25f, 0f)
            }
            axisRight.isEnabled = false
            setExtraOffsets(8f, 8f, 8f, 8f)
            val customRenderer = IndicatorAxisRenderer(
                viewPortHandler,
                xAxis,
                getTransformer(axisLeft.axisDependency)
            ).also {
                setXAxisRenderer(it)
            }
        }
    }

    /**
     * Sets the data to be displayed in the line chart. This method accepts a list of lists of Entry objects,
     * where each list represents a line dataset.
     *
     * @param chartData The data to set for the line chart, where each list represents a line dataset.
     */

    fun setDataToLineChart(chartData: List<List<Entry>>) {
        val color = ContextCompat.getColor(context, R.color.shimmer_background_color)
        val dataSets = chartData.mapIndexed { index, week ->
            LineDataSet(week, "Week ${index + 1}").apply {
                styleDataSet(this, color)
            }
        }
        binding.lineChart.data = LineData(dataSets)
        binding.lineChart.invalidate()
    }

    /**
     * Applies styling to the dataset for the line chart. This includes configurations such as line width,
     * value text size, color, and whether to draw circles and values.
     *
     * @param dataSet The dataset to apply styling to.
     * @param color The color to use for the dataset.
     */
    private fun styleDataSet(dataSet: LineDataSet, color: Int) {
        dataSet.apply {
            lineWidth = 6f
            valueTextSize = 15f
            mode = LineDataSet.Mode.CUBIC_BEZIER
            this.color = color
            cubicIntensity = 0.9f
            setCircleColor(color)
            setDrawCircles(true)
            setDrawValues(false)
            setDrawCircleHole(false)
            setDrawHorizontalHighlightIndicator(false)
            setDrawVerticalHighlightIndicator(false)
            enableDashedLine(0f, 0f, 0f)
        }
    }

    inner class ClaimsYAxisValueFormatter : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String = "${value.roundToInt()} %"
    }

    inner class MyAxisFormatter : IndexAxisValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String =
            xAxisValues.getOrNull(value.toInt() - 1) ?: ""
    }

    internal class IndicatorAxisRenderer(
        viewPortHandler: ViewPortHandler,
        xAxis: XAxis,
        trans: Transformer
    ) : XAxisRenderer(viewPortHandler, xAxis, trans) {

        private var indicatorWidth = 5f // Bold indicator width
        private var indicatorHeight = 20f // Larger indicator height
        private var midIndicatorWidth = 2f // Smaller and lighter indicator width
        private var midIndicatorHeight = 10f // Smaller indicator height
        private val boldIndicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK // Bold indicator color
            strokeWidth = indicatorWidth
        }
        private val lightIndicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.GRAY // Light indicator color
            strokeWidth = midIndicatorWidth
        }

        private fun getXLabelPositions(): FloatArray {
            var i = 0
            val positions = FloatArray(mXAxis.mEntryCount * 2)
            val centeringEnabled = mXAxis.isCenterAxisLabelsEnabled
            while (i < positions.size) {
                if (centeringEnabled) {
                    positions[i] = mXAxis.mCenteredEntries[i / 2]
                } else {
                    positions[i] = mXAxis.mEntries[i / 2]
                }
                positions[i + 1] = 0f
                i += 2
            }
            mTrans.pointValuesToPixel(positions)
            return positions
        }

        override fun renderAxisLine(c: Canvas?) {
            super.renderAxisLine(c)
            val positions = getXLabelPositions()
            var i = 0
            while (i < positions.size) {
                val x = positions[i]
                if (mViewPortHandler.isInBoundsX(x)) {
                    val y = mViewPortHandler.contentBottom()
                    // Draw the bold indicator
                    c?.drawLine(x, y, x, y + indicatorHeight, boldIndicatorPaint)

                    // For mid-indicators: Calculate and draw if there's a next label
                    if (i + 2 < positions.size) {
                        val midX = (x + positions[i + 2]) / 2
                        c?.drawLine(midX, y, midX, y + midIndicatorHeight, lightIndicatorPaint)
                    }
                }
                i += 2
            }
        }

        fun setIndicatorSize(
            boldWidth: Float,
            boldHeight: Float,
            lightWidth: Float,
            lightHeight: Float
        ) {
            this.indicatorWidth = boldWidth
            this.indicatorHeight = boldHeight
            this.midIndicatorWidth = lightWidth
            this.midIndicatorHeight = lightHeight
            boldIndicatorPaint.strokeWidth = boldWidth
            lightIndicatorPaint.strokeWidth = lightWidth
        }
    }

    fun setXAxisLabels(labels: ArrayList<String>) {
        xAxisValues.clear()
        xAxisValues.addAll(labels)
    }

    fun getLineChart(): LineChart = binding.lineChart

    fun getYaxisBackground():View = binding.yAxisBackground
}
