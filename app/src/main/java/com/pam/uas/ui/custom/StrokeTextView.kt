package com.pam.uas.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class StrokeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var strokeColor: Int = Color.TRANSPARENT
    private var strokeWidth: Float = 0f


    fun setStroke(width: Float, color: Int) {
        this.strokeWidth = width
        this.strokeColor = color
        invalidate()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (shadowRadius > 0) {
            this.strokeWidth = shadowRadius
            this.strokeColor = shadowColor

            setShadowLayer(0f, 0f, 0f, 0)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (strokeWidth > 0) {
            val originalColor = textColors

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = strokeWidth
            paint.strokeJoin = Paint.Join.ROUND
            paint.strokeCap = Paint.Cap.ROUND
            setTextColor(strokeColor)

            super.onDraw(canvas)

            paint.style = Paint.Style.FILL
            setTextColor(originalColor)
        }

        super.onDraw(canvas)
    }
}
