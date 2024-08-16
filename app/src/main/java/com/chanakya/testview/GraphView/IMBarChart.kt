package com.chanakya.testview.GraphView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import com.chanakya.testview.databinding.ImBarChartBinding
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler
import com.google.android.material.card.MaterialCardView

/**
 * Custom MaterialCardView that encapsulates a BarChart for easier reuse across the app.
 * It simplifies the process of initializing and displaying a bar chart with custom rounded bars,
 * color settings, and an average line marker.
 */
class IMBarChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    private val binding: ImBarChartBinding
    private val xAxisValues = ArrayList<String>()
    lateinit var barChart: BarChart

    // on below line we are creating
    // a variable for bar data
    lateinit var barData: BarData

    // on below line we are creating a
    // variable for bar data set
    lateinit var barDataSet: BarDataSet

    init {
        // Inflate the layout for this component
        binding = ImBarChartBinding.inflate(LayoutInflater.from(context), this, true)
        barChart = binding.barChart
    }

    /**
     * Initializes the bar chart with data, colors, and customizations.
     * @param barEntriesList List of BarEntry objects representing the data points.
     * @param barColors List of integers representing the colors for each bar.
     */
    fun initializedBarChart(barEntriesList: ArrayList<BarEntry>, barColors: ArrayList<Int>) {
        barDataSet = BarDataSet(barEntriesList, "Bar Chart Data").apply {
            colors = barColors // Apply the colors to the dataset
            valueTextColor = Color.BLACK
            valueTextSize = 0f // Set to 0f to not draw values
        }

        barData = BarData(barDataSet)
        barChart.data = barData
        barData.barWidth = 0.5f
        customizeChartAppearance()
    }

    /**
     * Customizes the appearance of the bar chart, including axis settings and adding a limit line.
     */
    private fun customizeChartAppearance() {
        with(barChart) {
            description.isEnabled = false
            axisLeft.apply {
                isEnabled = true // Re-enable the Y-Axis for the limit line
                setDrawAxisLine(false)
                setDrawLabels(false)
                setDrawGridLines(false)
                addLimitLine(createAverageLimitLine()) // Add average limit line
            }
            axisRight.isEnabled = false // Keep right axis disabled
            legend.isEnabled = false
            xAxis.apply {
                setDrawAxisLine(false)
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                setDrawGridLines(false)
                if (xAxisValues.isNotEmpty()) {
                    valueFormatter = MyAxisFormatter()
                }
            }
            renderer = RoundedBarChartRenderer(this, animator, viewPortHandler, 16)
            invalidate() // Refresh the chart
        }
    }

    /**
     * Creates and configures a limit line representing the average value of the data set.
     * @return A configured LimitLine object.
     */
    private fun createAverageLimitLine(): LimitLine {
        val average =
            calculateAverage(barData.getDataSetByIndex(0)) // Assuming your data set is at index 0
        return LimitLine(average, "AVG: $average").apply {
            lineWidth = 2f
            lineColor = Color.parseColor("#DB5E10")
            textColor = Color.parseColor("#DB5E10")
            textSize = 12f
            labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            enableDashedLine(10f, 10f, 0f)
        }
    }

    /**
     * Calculates the average value of a given bar data set.
     * @param dataSet The IBarDataSet to calculate the average for.
     * @return The average value as a Float.
     */
    fun calculateAverage(dataSet: IBarDataSet): Float {
        var total = 0f
        for (i in 0 until dataSet.entryCount) {
            total += dataSet.getEntryForIndex(i).y
        }
        return total / dataSet.entryCount
    }

    /**
     * Custom formatter for the X-axis labels.
     */
    inner class MyAxisFormatter : IndexAxisValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String =
            xAxisValues.getOrNull(value.toInt() - 1) ?: ""
    }

    /**
     * Sets custom labels for the X-axis.
     * @param labels The list of strings to use as labels.
     */
    fun setXAxisLabels(labels: ArrayList<String>) {
        xAxisValues.clear()
        xAxisValues.addAll(labels)
    }

    /**
     * Provides access to the underlying BarChart object.
     * @return The BarChart instance.
     */
    fun getLineChart(): BarChart = binding.barChart

    class RoundedBarChartRenderer(
        chart: BarDataProvider,
        animator: ChartAnimator,
        viewPortHandler: ViewPortHandler,
        private val mRadius: Int
    ) : BarChartRenderer(chart, animator, viewPortHandler)
    {

        private val mBarShadowRectBuffer = RectF()

        override fun drawHighlighted(c: Canvas, indices: Array<Highlight>) {
            val barData = mChart.barData

            indices.forEach { high ->
                val set = barData.getDataSetByIndex(high.dataSetIndex)

                if (set == null || !set.isHighlightEnabled) return@forEach

                val e = set.getEntryForXValue(high.x, high.y)

                if (!isInBoundsX(e, set)) return@forEach

                val trans = mChart.getTransformer(set.axisDependency)

                mHighlightPaint.color = set.highLightColor
                mHighlightPaint.alpha = set.highLightAlpha

                val isStack = high.stackIndex >= 0 && e.isStacked

                val y1: Float
                val y2: Float

                if (isStack) {
                    if (mChart.isHighlightFullBarEnabled) {
                        y1 = e.positiveSum
                        y2 = -e.negativeSum
                    } else {
                        val range = e.ranges[high.stackIndex]
                        y1 = range.from
                        y2 = range.to
                    }
                } else {
                    y1 = e.y
                    y2 = 0f
                }

                prepareBarHighlight(e.x, y1, y2, barData.barWidth / 2f, trans)

                setHighlightDrawPos(high, mBarRect)

                c.drawRoundRect(mBarRect, mRadius.toFloat(), mRadius.toFloat(), mHighlightPaint)
            }
        }

        override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
            val trans = mChart.getTransformer(dataSet.axisDependency)

            mBarBorderPaint.color = dataSet.barBorderColor
            mBarBorderPaint.strokeWidth = Utils.convertDpToPixel(dataSet.barBorderWidth.toFloat())

            val drawBorder = dataSet.barBorderWidth > 0f

            val phaseX = mAnimator.phaseX
            val phaseY = mAnimator.phaseY

            if (mChart.isDrawBarShadowEnabled) {
                mShadowPaint.color = dataSet.barShadowColor

                val barData = mChart.barData
                val barWidth = barData.barWidth
                val barWidthHalf = barWidth / 2.0f
                var x: Float

                for (i in 0 until Math.min(
                    Math.ceil(dataSet.entryCount.toDouble() * phaseX).toInt(), dataSet.entryCount
                )) {
                    val e = dataSet.getEntryForIndex(i)

                    x = e.x

                    mBarShadowRectBuffer.left = x - barWidthHalf
                    mBarShadowRectBuffer.right = x + barWidthHalf

                    trans.rectValueToPixel(mBarShadowRectBuffer)

                    if (!mViewPortHandler.isInBoundsLeft(mBarShadowRectBuffer.right)) continue

                    if (!mViewPortHandler.isInBoundsRight(mBarShadowRectBuffer.left)) break

                    mBarShadowRectBuffer.top = mViewPortHandler.contentTop()
                    mBarShadowRectBuffer.bottom = mViewPortHandler.contentBottom()

                    c.drawRoundRect(
                        mBarShadowRectBuffer,
                        mRadius.toFloat(),
                        mRadius.toFloat(),
                        mShadowPaint
                    )
                }
            }

            val buffer = mBarBuffers[index]
            buffer.setPhases(phaseX, phaseY)
            buffer.setDataSet(index)
            buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
            buffer.setBarWidth(mChart.barData.barWidth)

            buffer.feed(dataSet)

            trans.pointValuesToPixel(buffer.buffer)

            val isSingleColor = dataSet.colors.size == 1

            if (isSingleColor) {
                mRenderPaint.color = dataSet.color
            }

            for (j in 0 until buffer.size() step 4) {
                if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) continue

                if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j])) break

                if (!isSingleColor) {
                    mRenderPaint.color = dataSet.getColor(j / 4)
                }

                dataSet.gradientColor?.let {
                    val gradientColor = it
                    mRenderPaint.shader = LinearGradient(
                        buffer.buffer[j], buffer.buffer[j + 3],
                        buffer.buffer[j], buffer.buffer[j + 1],
                        gradientColor.startColor, gradientColor.endColor,
                        Shader.TileMode.MIRROR
                    )
                }

                dataSet.getGradientColors()?.let {
                    mRenderPaint.shader = LinearGradient(
                        buffer.buffer[j], buffer.buffer[j + 3],
                        buffer.buffer[j], buffer.buffer[j + 1],
                        it[j / 4].startColor, it[j / 4].endColor,
                        Shader.TileMode.MIRROR
                    )
                }

                c.drawRoundRect(
                    buffer.buffer[j], buffer.buffer[j + 1],
                    buffer.buffer[j + 2], buffer.buffer[j + 3],
                    mRadius.toFloat(), mRadius.toFloat(), mRenderPaint
                )

                if (drawBorder) {
                    c.drawRoundRect(
                        buffer.buffer[j], buffer.buffer[j + 1],
                        buffer.buffer[j + 2], buffer.buffer[j + 3],
                        mRadius.toFloat(), mRadius.toFloat(), mBarBorderPaint
                    )
                }
            }
        }
    }

}
