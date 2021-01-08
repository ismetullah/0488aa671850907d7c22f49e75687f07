package com.asmatullah.spaceapp.common.uikit.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.asmatullah.spaceapp.common.uikit.R
import com.asmatullah.spaceapp.common.uikit.common.getDarkerColor

class CustomButton : LinearLayout {

    private var drawable: Drawable? = null
    private var drawablePressed: Drawable? = null
    private var bottomHeight = 5
    private var strokeWidth: Int = 1

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        resId: Int
    ) : super(context, attrs, resId) {
        init(context, attrs, resId)
    }

    private fun init(
        context: Context,
        attrs: AttributeSet?,
        resId: Int
    ) {
        drawable = ContextCompat.getDrawable(context, R.drawable.background_custom_button)
        drawablePressed =
            ContextCompat.getDrawable(context, R.drawable.background_custom_button_pressed)

        var colorLight: Int = ContextCompat.getColor(context, R.color.green_900);
        var colorStroke: Int = -1
        var colorDark: Int = -1
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomButton)
            colorLight = typedArray.getColor(R.styleable.CustomButton_colorLight, colorLight)
            colorStroke = typedArray.getColor(R.styleable.CustomButton_colorStroke, -1)
            colorDark = typedArray.getColor(R.styleable.CustomButton_colorDark, -1)
            val draw = typedArray.getDrawable(R.styleable.CustomButton_drawable)
            draw?.let { drawable = it }
            val drawPressed = typedArray.getDrawable(R.styleable.CustomButton_drawablePressed)
            drawPressed?.let { drawablePressed = it }
            bottomHeight = typedArray.getInt(R.styleable.CustomButton_bottomHeight, 5)
            strokeWidth = typedArray.getInt(R.styleable.CustomButton_colorStrokeWidth, strokeWidth)
            typedArray.recycle()
        }
        if (colorDark == -1)
            colorDark =
                getDarkerColor(colorLight)

        val normalDrawable = getNormalDrawable(colorLight, colorStroke, colorDark)
        val pressedDrawable = getPressedDrawable(colorLight, colorStroke)

        val stateList = StateListDrawable()
        stateList.addState(intArrayOf(android.R.attr.state_pressed), pressedDrawable)
        stateList.addState(intArrayOf(), normalDrawable)

        background = stateList
        isClickable = true
        isFocusable = true

        initTouchListener()

        setNormalState()
    }

    private fun initTouchListener() {
        setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    setPressedState()
                    true
                }
                MotionEvent.ACTION_UP -> {
                    if (event.x < v.width && event.y < v.height) {
                        performClick()
                    }
                    setNormalState()
                    true
                }
                else -> true
            }
        }
    }

    private fun setNormalState() {
        setPadding(0, 0, 0, getDpAsPixels(bottomHeight, context).toInt())
        isPressed = false
    }

    private fun setPressedState() {
        setPadding(0, getDpAsPixels(bottomHeight, context).toInt(), 0, 0)
        isPressed = true
    }

    private fun getPressedDrawable(colorLight: Int, colorStroke: Int): LayerDrawable {
        val layerList = drawablePressed as LayerDrawable
        val item =
            layerList.findDrawableByLayerId(R.id.colorPressed) as GradientDrawable
        item.setColor(colorLight)
        if (colorStroke != -1) {
            item.setStroke(getDpAsPixels(strokeWidth, context).toInt(), colorStroke)
        }
        return layerList
    }

    private fun getNormalDrawable(
        colorLight: Int,
        colorStroke: Int,
        colorDark: Int
    ): LayerDrawable {
        val layerList = drawable as LayerDrawable
        val itemLight =
            layerList.findDrawableByLayerId(R.id.colorLight) as GradientDrawable
        val itemDark =
            layerList.findDrawableByLayerId(R.id.colorDark) as GradientDrawable
        itemLight.setColor(colorLight)
        if (colorStroke != -1) {
            itemLight.setStroke(getDpAsPixels(strokeWidth, context).toInt(), colorStroke)
        }
        itemDark.setColor(colorDark)
        return layerList
    }

    private fun getDpAsPixels(sizeInDp: Int, context: Context): Float {
        val scale = context.resources.displayMetrics.density
        return (sizeInDp * scale + 0.5f)
    }
}