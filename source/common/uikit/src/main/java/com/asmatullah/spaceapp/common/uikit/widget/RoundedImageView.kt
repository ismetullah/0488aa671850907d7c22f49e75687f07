package com.asmatullah.spaceapp.common.uikit.widget

import android.content.Context
import android.graphics.*
import android.graphics.Path.FillType
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.asmatullah.spaceapp.common.uikit.R

class RoundedImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    private var maskPath: Path? = null
    private val maskPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var cornerRadius = 20f

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView)
            cornerRadius =
                typedArray.getDimension(R.styleable.RoundedImageView_cornerRadius, cornerRadius)
            typedArray.recycle()
        }

        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        maskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        maskPaint.color = Color.TRANSPARENT

    }

    override fun onDraw(canvas: Canvas) {
        if (canvas.isOpaque) {
            canvas.saveLayerAlpha(
                0f, 0f, canvas.width.toFloat(),
                canvas.height.toFloat(), 255
            )
        }

        super.onDraw(canvas)

        maskPath?.apply {
            canvas.drawPath(this, maskPaint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != oldw || h != oldh) {
            generateMaskPath(w, h)
        }
    }

    private fun generateMaskPath(w: Int, h: Int) {
        maskPath = Path()
        maskPath!!.addRoundRect(
            RectF(0f, 0f, w.toFloat(), h.toFloat()), cornerRadius, cornerRadius, Path.Direction.CW
        )
        maskPath!!.fillType = FillType.INVERSE_WINDING
    }
}