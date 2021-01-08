package com.asmatullah.spaceapp.common.uikit.common

import android.app.Activity
import android.content.ContextWrapper
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt


@ColorInt
fun Activity.colorFromTheme(@AttrRes attrId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrId, typedValue, true)
    val colorInt = typedValue.data
    return Color.argb(
        Color.alpha(colorInt),
        Color.red(colorInt),
        Color.green(colorInt),
        Color.blue(colorInt)
    )
}

@ColorInt
fun View.colorFromTheme(@AttrRes attrId: Int): Int {
    var c = context as? ContextWrapper
    while (c != null) {
        if (c is Activity) return c.colorFromTheme(attrId)
        c = c.baseContext as? ContextWrapper
    }
    return 0
}

@ColorInt
fun Fragment.colorFromTheme(@AttrRes attrId: Int): Int = requireActivity().colorFromTheme(attrId)

fun Activity.resFromTheme(@AttrRes attrId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrId, typedValue, true)
    return typedValue.resourceId
}

fun View.resFromTheme(@AttrRes attrId: Int): Int {
    var c = context as? ContextWrapper
    while (c != null) {
        if (c is Activity) return c.resFromTheme(attrId)
        c = c.baseContext as? ContextWrapper
    }
    return 0
}

fun Fragment.resFromTheme(@AttrRes attrId: Int): Int = requireActivity().resFromTheme(attrId)

fun View.showIf(value: Boolean) {
    visibility = if (value) View.VISIBLE else View.GONE
}

fun getDarkerColor(color: Int, step: Float = 0.1f): Int {
    val r = Color.red(color) - (255F * step).roundToInt()
    val g = Color.green(color) - (255F * step).roundToInt()
    val b = Color.blue(color) - (255F * step).roundToInt()
    return Color.rgb(
        max(r, 0),
        max(g, 0),
        max(b, 0)
    )
}

fun getLighterColor(color: Int, step: Float = 0.1f): Int {
    val r = Color.red(color) + (255F * step).roundToInt()
    val g = Color.green(color) + (255F * step).roundToInt()
    val b = Color.blue(color) + (255F * step).roundToInt()
    return Color.rgb(
        min(r, 255),
        min(g, 255),
        min(b, 255)
    )
}