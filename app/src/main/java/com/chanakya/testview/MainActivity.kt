package com.chanakya.testview

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.chanakya.testview.databinding.ActivityMainBinding
import com.github.mikephil.charting.animation.Easing
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
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
            it.lineChart.setXAxisLabels(
                arrayListOf(
                    "7d",
                    "14d",
                    "29d",
                    "45d",
                    "60d",
                    "1y",
                    "3yr",
                    "5yr",
                    "10yr",
                    "15yr",
                    "20yr"
                )
            )
            it.lineChart.invalidateChart()
            it.lineChart.setDataToLineChart(listOf(week1(), week2(), week3(), week4()))
        }


    }


    private fun week1() = arrayListOf(Entry(0f, 13f), Entry(1f, 13f))
    private fun week2() = arrayListOf(Entry(1f, 14f), Entry(2f, 14f))
    private fun week3() = arrayListOf(Entry(3f, 15f), Entry(6f, 15f))
    private fun week4() = arrayListOf(Entry(5f, 16f), Entry(9f, 16f))

    inner class ClaimsYAxisValueFormatter : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String = "${value.roundToInt()} %"
    }

    inner class MyAxisFormatter : IndexAxisValueFormatter() {
        private val items =
            listOf("7d", "14d", "29d", "45d", "60d", "1y", "3yr", "5yr", "10yr", "15yr", "20yr")

        override fun getAxisLabel(value: Float, axis: AxisBase?): String =
            items.getOrNull(value.toInt() - 1) ?: ""
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
}
