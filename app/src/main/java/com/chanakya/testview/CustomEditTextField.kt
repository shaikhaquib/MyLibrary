package com.chanakya.testview

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes

class CustomTextField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var labelText: String = ""
    private var cornerRadius: Float = 0f
    private var borderColor: Int = 0
    private var cardElevation: Float = 0f
    private var startDrawable: Drawable? = null
    private var endDrawable: Drawable? = null

    private val labelPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private var labelX: Float = 0f
    private var labelY: Float = 0f
    private var labelScale: Float = 1f

    private val defaultLabelSizePx: Float
    private val smallLabelSizePx: Float
    private val drawablePadding: Int = 16.dpToPx() // Increased padding

    private var isLabelFloating = false

    init {
        defaultLabelSizePx = 16.dpToPx().toFloat()
        smallLabelSizePx = 12.dpToPx().toFloat()

        context.withStyledAttributes(attrs, R.styleable.CustomTextField) {
            labelText = getString(R.styleable.CustomTextField_ti_labelText) ?: hint?.toString() ?: ""
            cornerRadius = getDimension(R.styleable.CustomTextField_ti_cornerRadius, 16f)
            borderColor = getColor(R.styleable.CustomTextField_ti_borderColor,
                ContextCompat.getColor(context, android.R.color.darker_gray))
            cardElevation = getDimension(R.styleable.CustomTextField_ti_cardElevation, 4f)
            startDrawable = getDrawable(R.styleable.CustomTextField_ti_startDrawable)
            endDrawable = getDrawable(R.styleable.CustomTextField_ti_endDrawable)
        }

        background = null
        hint = null
        elevation = cardElevation

        labelPaint.textSize = defaultLabelSizePx

        setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                animateLabelToTop()
            } else if (text.isNullOrEmpty()) {
                animateLabelToBottom()
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (!isLabelFloating) {
            labelX = paddingLeft.toFloat() + getDrawablePaddingLeft()
            labelY = height / 2f + labelPaint.textSize / 4
        }
        updateTextPadding()
    }

    override fun onDraw(canvas: Canvas) {
        // Draw border
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), cornerRadius, cornerRadius, paint)

        // Draw label
        labelPaint.color = if (isFocused) borderColor else ContextCompat.getColor(context, android.R.color.darker_gray)
        canvas.drawText(labelText, labelX, labelY, labelPaint)

        // Draw drawables
        startDrawable?.let {
            val left = paddingLeft
            val top = (height - it.intrinsicHeight) / 2
            it.setBounds(left, top, left + it.intrinsicWidth, top + it.intrinsicHeight)
            it.draw(canvas)
        }
        endDrawable?.let {
            val right = width - paddingRight
            val top = (height - it.intrinsicHeight) / 2
            it.setBounds(right - it.intrinsicWidth, top, right, top + it.intrinsicHeight)
            it.draw(canvas)
        }

        super.onDraw(canvas)
    }

    private fun animateLabelToTop() {
        if (isLabelFloating) return
        isLabelFloating = true

        val targetY = paddingTop + smallLabelSizePx / 2

        val animX = ObjectAnimator.ofFloat(this, "labelX", labelX, paddingLeft.toFloat() + getDrawablePaddingLeft())
        val animY = ObjectAnimator.ofFloat(this, "labelY", labelY, targetY)
        val animScale = ObjectAnimator.ofFloat(this, "labelScale", 1f, smallLabelSizePx / defaultLabelSizePx)

        AnimatorSet().apply {
            playTogether(animX, animY, animScale)
            duration = 200
            interpolator = DecelerateInterpolator()
            start()
        }

        updateTextPadding()
    }

    private fun animateLabelToBottom() {
        if (!isLabelFloating) return
        isLabelFloating = false

        val targetY = height / 2f + defaultLabelSizePx / 4

        val animX = ObjectAnimator.ofFloat(this, "labelX", labelX, paddingLeft.toFloat() + getDrawablePaddingLeft())
        val animY = ObjectAnimator.ofFloat(this, "labelY", labelY, targetY)
        val animScale = ObjectAnimator.ofFloat(this, "labelScale", labelScale, 1f)

        AnimatorSet().apply {
            playTogether(animX, animY, animScale)
            duration = 200
            interpolator = DecelerateInterpolator()
            start()
        }

        updateTextPadding()
    }

    private fun updateTextPadding() {
        val topPadding = if (isLabelFloating) {
            (paddingTop + defaultLabelSizePx).toInt()
        } else {
            paddingTop
        }

        val leftPadding = paddingLeft + getDrawablePaddingLeft()
        val rightPadding = paddingRight + getDrawablePaddingRight()

        // Set compound drawables with padding
        setCompoundDrawablesRelativeWithIntrinsicBounds(startDrawable, null, endDrawable, null)
        compoundDrawablePadding = drawablePadding

        // Set text padding
        setPadding(leftPadding, topPadding, rightPadding, paddingBottom)
    }

    private fun getDrawablePaddingLeft(): Int {
        return if (startDrawable != null) startDrawable!!.intrinsicWidth + drawablePadding else 0
    }

    private fun getDrawablePaddingRight(): Int {
        return if (endDrawable != null) endDrawable!!.intrinsicWidth + drawablePadding else 0
    }

    // Setter methods for animation properties
    fun setLabelX(x: Float) {
        labelX = x
        invalidate()
    }

    fun setLabelY(y: Float) {
        labelY = y
        invalidate()
    }

    fun setLabelScale(scale: Float) {
        labelScale = scale
        labelPaint.textSize = defaultLabelSizePx * scale
        invalidate()
    }

    // Setter methods for custom properties
    fun setCornerRadius(radius: Float) {
        cornerRadius = radius
        invalidate()
    }

    fun setBorderColor(color: Int) {
        borderColor = color
        invalidate()
    }

    fun setCardElevation(elevation: Float) {
        cardElevation = elevation
        this.elevation = cardElevation
    }

    fun setStartDrawable(drawable: Drawable?) {
        startDrawable = drawable
        updateTextPadding()
    }

    fun setEndDrawable(drawable: Drawable?) {
        endDrawable = drawable
        updateTextPadding()
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}