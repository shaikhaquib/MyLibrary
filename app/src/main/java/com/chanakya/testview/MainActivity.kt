package com.chanakya.testview

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.chanakya.testview.databinding.ActivityMainBinding
import com.google.android.material.slider.Slider
import com.chanakya.testview.view.IMContinuousSliderWithLabel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupLineChart(binding.lineChart)

        Handler(Looper.getMainLooper()).postDelayed({
            updateGraphGradient(binding.lineChart, Color.CYAN, Color.TRANSPARENT)
        }, 1000)


    }

    private fun setupLineChart(lineChart: LineChart) {
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)

        // Customizing x and y axis
        lineChart.xAxis.isEnabled = false
        lineChart.axisLeft.isEnabled = false
        lineChart.axisRight.isEnabled = false

        // Removing description label and legend
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false

        val values = ArrayList<Entry>()
        // Sample data
        for (i in 0..20) {
            val value = (Math.random() * 100).toFloat()
            values.add(Entry(i.toFloat(), value))
        }

        val lineDataSet = LineDataSet(values, "DataSet 1").apply {
            setDrawIcons(false)
            color = Color.RED
            setCircleColor(Color.BLACK)
            lineWidth = 2f
            circleRadius = 3f
            setDrawCircleHole(false)
            valueTextSize = 9f
            setDrawFilled(true)
            setDrawValues(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawCircles(false) // Remove coordinate circles

            // Gradient fill
            val gradientColors = intArrayOf(Color.CYAN, Color.TRANSPARENT)
            val gradient = LinearGradient(
                0f,
                0f,
                0f,
                lineChart.height.toFloat(),
                gradientColors,
                null,
                Shader.TileMode.MIRROR
            )
            val paint = Paint()
            paint.shader = gradient
            fillDrawable =
                ContextCompat.getDrawable(applicationContext, R.drawable.line_chart_gradient)
        }

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(lineDataSet) // add the datasets

        // Create a data object with the datasets
        val data = LineData(dataSets)
        lineChart.data = data
        // Set the viewport to show only a limited number of entries at a time
        lineChart.setVisibleXRangeMaximum(4f) // for example, 20 points visible at a time
        lineChart.moveViewToX(10f) // moves the left edge of the visible range to x-index 10


        lineChart.invalidate() // refresh
    }

    fun updateGraphGradient(lineChart: LineChart, colorStart: Int, colorEnd: Int) {
        // Ensure there is data and at least one dataset present
        if (lineChart.data != null && lineChart.data.dataSetCount > 0) {
            val lineDataSet = lineChart.data.getDataSetByIndex(0) as LineDataSet

            // Create a new LinearGradient
            // Create and set the custom GradientDrawable
            val gradientDrawable = GradientDrawable(colorStart, colorEnd)
            lineDataSet.fillDrawable = gradientDrawable

            lineChart.invalidate() // Refresh the chart to apply the new gradient

        }
    }

    class GradientDrawable(private val colorStart: Int, private val colorEnd: Int) : Drawable() {
        private val paint = Paint()

        override fun draw(canvas: Canvas) {
            val rect = RectF(bounds)
            paint.shader = LinearGradient(
                rect.left, rect.top, rect.left, rect.bottom,
                colorStart, colorEnd,
                Shader.TileMode.CLAMP
            )
            canvas.drawRect(rect, paint)
        }

        override fun setAlpha(alpha: Int) {
            paint.alpha = alpha
        }

        override fun setColorFilter(colorFilter: ColorFilter?) {
            paint.colorFilter = colorFilter
        }

        override fun getOpacity(): Int = paint.alpha
    }
}

