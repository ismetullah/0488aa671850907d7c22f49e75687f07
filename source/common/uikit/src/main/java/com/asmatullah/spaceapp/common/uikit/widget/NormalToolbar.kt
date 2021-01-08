package com.asmatullah.spaceapp.common.uikit.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.asmatullah.spaceapp.common.uikit.R
import kotlinx.android.synthetic.main.widget_toolbar.view.*

class NormalToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mTitleTextView: AppCompatTextView

    init {
        val view = inflate(context, R.layout.widget_toolbar, this)

        var hideNavigationIcon = false

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NormalToolbar)
            hideNavigationIcon = typedArray.getBoolean(R.styleable.NormalToolbar_hideNavigationIcon, false)
            typedArray.recycle()
        }

        mTitleTextView = view.findViewById(R.id.toolbar_title)

        if (hideNavigationIcon) {
            toolbar.navigationIcon = null
        }
    }

    inline operator fun invoke(crossinline block: Toolbar.() -> Unit) {
        toolbar.block()
    }

    fun setNavigationIcon(@DrawableRes icon: Int) {
        toolbar.navigationIcon = ContextCompat.getDrawable(context, icon)
    }

    fun hideNavigationIcon() {
        toolbar.navigationIcon = null
    }

    fun setTitle(@StringRes resId: Int) {
        setTitle(context.getText(resId))
    }

    fun setTitle(title: CharSequence?) {
        mTitleTextView.text = title
    }

    fun getTitle() = mTitleTextView.text
}