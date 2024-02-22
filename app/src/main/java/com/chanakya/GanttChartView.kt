package com.chanakya

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.chanakya.testview.R
import java.util.Calendar
import java.util.Locale

class GanttChartView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var tasks: List<Task> = emptyList()
    private val barPaint = Paint()
    private val axisPaint = Paint()
    private val linePaint = Paint()
    private val backgroundPaint = Paint()
    private val dp4 = context.resources.displayMetrics.density * 4

    init {
        barPaint.color = ContextCompat.getColor(context, R.color.shimmer_background_color)
        barPaint.strokeWidth = dp4

        axisPaint.color = Color.BLACK
        axisPaint.strokeWidth = 5f
        axisPaint.textSize = 30f

        linePaint.color = Color.GRAY
        linePaint.strokeWidth = 2f
        linePaint.pathEffect = android.graphics.DashPathEffect(floatArrayOf(10f, 10f), 0f)

        backgroundPaint.color = Color.LTGRAY
    }

    fun setTasks(tasks: List<Task>) {
        Log.d("GanttChartView", "Setting ${tasks.size} tasks.")
        this.tasks = tasks
        invalidate() // Invalidate the view to trigger a redraw
        requestLayout() // Request layout to recalculate the view's dimensions
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d("GanttChartView", "Drawing Gantt chart with ${tasks.size} tasks.")

        if (tasks.isEmpty()) {
            return
        }

        val minDate = tasks.minOf { it.startDate }
        val maxDate = tasks.maxOf { it.endDate }

        val topMargin = 100
        val bottomMargin = 100
        val width = width - 200 // Adjust for padding
        val height = dp4

        // Draw background for Y-axis
        canvas.drawRect(0f, 0f, 100f, (height * tasks.size + topMargin + bottomMargin).toFloat(), backgroundPaint)

        var top = topMargin

        for (task in tasks) {
            val left = dateToPixel(task.startDate, minDate, maxDate, width) + 100 // Adjust for padding
            val right = dateToPixel(task.endDate, minDate, maxDate, width) + 100 // Adjust for padding
            val rect = RectF(left.toFloat(), top.toFloat(), right.toFloat(), (top + height).toFloat())
            canvas.drawRoundRect(rect, dp4, dp4, barPaint)

            // Draw horizontal dotted line
            canvas.drawLine(100f, top.toFloat(), (width + 100).toFloat(), top.toFloat(), linePaint)

            // Draw return percentage on Y-axis
            canvas.drawText("${task.percentage}%", 10f, (top + height / 2).toFloat(), axisPaint)

            top += bottomMargin
        }

        // Draw horizontal dotted line for the last task
        canvas.drawLine(100f, top.toFloat(), (width + 100).toFloat(), top.toFloat(), linePaint)

        // Draw X-axis at the bottom
        canvas.drawLine(100f, top.toFloat(), (width + 100).toFloat(), top.toFloat(), axisPaint)

        // Draw custom labels on X-axis
        val labels = arrayOf("7d", "14d", "29d", "45d", "60d", "1yr")
        val intervals = arrayOf(7, 14, 29, 45, 60, 365) // Corresponding days for the labels
        for (i in labels.indices) {
            val xPosition = dateToPixel(intervals[i].toLong(), minDate, maxDate, width) + 100
            canvas.drawText(labels[i], xPosition.toFloat(), (top + 40).toFloat(), axisPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredHeight = (tasks.size * (dp4 + 100) + 100).toInt() // Calculate the desired height based on tasks
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = resolveSize(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    private fun dateToPixel(date: Long, minDate: Long, maxDate: Long, width: Int): Int {
        val relativePosition = (date - minDate).toDouble() / (maxDate - minDate)
        return (relativePosition * width).toInt()
    }

    data class Task(val name: String, val startDate: Long, val endDate: Long, val percentage: Int)
}
