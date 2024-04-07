package com.chanakya.testview.shadow

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable

class ShadowViewDrawable(
    private val shadowProperty: ShadowProperty,
    color: Int,
    private val rx: Float,
    private val ry: Float
) : Drawable() {
    private val paint: Paint
    private val bounds = RectF()
    private var width = 0
    private var height = 0
    private val shadowOffset: Int
    private var drawRect: RectF
    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        if (bounds.right - bounds.left > 0 && bounds.bottom - bounds.top > 0) {
            this.bounds.left = bounds.left.toFloat()
            this.bounds.right = bounds.right.toFloat()
            this.bounds.top = bounds.top.toFloat()
            this.bounds.bottom = bounds.bottom.toFloat()
            width = (this.bounds.right - this.bounds.left).toInt()
            height = (this.bounds.bottom - this.bounds.top).toInt()

//            drawRect = new RectF(shadowOffset, shadowOffset, width - shadowOffset, height - shadowOffset);
//            drawRect = new RectF(0, 0, width, height - shadowOffset);
            val shadowSide = shadowProperty.shadowSide
            val left =
                if (shadowSide and ShadowProperty.LEFT == ShadowProperty.LEFT) shadowOffset else 0
            val top =
                if (shadowSide and ShadowProperty.TOP == ShadowProperty.TOP) shadowOffset else 0
            val right =
                width - if (shadowSide and ShadowProperty.RIGHT == ShadowProperty.RIGHT) shadowOffset else 0
            val bottom =
                height - if (shadowSide and ShadowProperty.BOTTOM == ShadowProperty.BOTTOM) shadowOffset else 0
            drawRect = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
            invalidateSelf()
        }
    }

    private val srcOut = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)

    init {
        shadowOffset = shadowProperty.shadowOffset
        paint = Paint()
        paint.isAntiAlias = true
        /**
         * 解决旋转时的锯齿问题
         */
        paint.isFilterBitmap = true
        paint.isDither = true
        paint.style = Paint.Style.FILL
        paint.color = color
        /**
         * 设置阴影
         */
        paint.setShadowLayer(
            shadowProperty.shadowRadius.toFloat(),
            shadowProperty.shadowDx.toFloat(),
            shadowProperty.shadowDy.toFloat(),
            shadowProperty.shadowColor
        )
        drawRect = RectF()
    }

    override fun draw(canvas: Canvas) {
        paint.setXfermode(null)
        canvas.drawRoundRect(
            drawRect,
            rx, ry,
            paint
        )
        paint.setXfermode(srcOut)
        //        paint.setColor(Color.TRANSPARENT);
        canvas.drawRoundRect(drawRect, rx, ry, paint)
    }

    fun setColor(color: Int): ShadowViewDrawable {
        paint.color = color
        return this
    }

    override fun setAlpha(alpha: Int) {}
    override fun setColorFilter(cf: ColorFilter?) {}
    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }
}