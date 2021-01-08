package com.asmatullah.spaceapp.common.uikit.widget

import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.StringRes

sealed class TextContainer {
    companion object {
        operator fun invoke(@StringRes textRes: Int, @ColorInt textColor: Int? = null) =
            Res(
                textRes,
                textColor
            )

        operator fun invoke(text: String, @ColorInt textColor: Int? = null) =
            Raw(
                text,
                textColor
            )
    }

    abstract fun applyTo(textView: TextView)

    class Res(
        @StringRes private val textRes: Int,
        @ColorInt private val textColor: Int? = null
    ) : TextContainer() {
        override fun applyTo(textView: TextView) {
            textView.setText(textRes)
            textColor?.let { textView.setTextColor(it) }
        }
    }

    class Raw(
        private val text: String,
        @ColorInt private val textColor: Int? = null
    ) : TextContainer() {
        override fun applyTo(textView: TextView) {
            textView.text = text
            textColor?.let { textView.setTextColor(it) }
        }
    }
}