package com.asmatullah.spaceapp.common.uikit.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.asmatullah.spaceapp.common.uikit.R

class CustomSeekbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var mTitleTextView: AppCompatTextView
    private var mMinTextView: AppCompatTextView
    private var mMaxTextView: AppCompatTextView
    private var mSeekBar: AppCompatSeekBar

    private var onSeekBarChangeListener: ((seekBar: SeekBar?, progress: Int) -> Unit)? = null

    var min: Int = 0
        set(value) {
            field = value
            mMinTextView.text = value.toString()
        }
    var max: Int = 5
        set(value) {
            field = value
            mSeekBar.max = value - min
            mMaxTextView.text = value.toString()
        }
    var title: String = ""
        set(value) {
            field = value
            updateTitle()
        }

    init {
        val view = inflate(context, R.layout.widget_custom_seekbar, this)

        mTitleTextView = view.findViewById(R.id.tvTitle)
        mSeekBar = view.findViewById(R.id.seekBar)
        mMinTextView = view.findViewById(R.id.tvMin)
        mMaxTextView = view.findViewById(R.id.tvMax)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomSeekbar)
            min = typedArray.getInteger(R.styleable.CustomSeekbar_min, 0)
            max = typedArray.getInteger(R.styleable.CustomSeekbar_max, 100)
            title = typedArray.getString(R.styleable.CustomSeekbar_title) ?: ""
            typedArray.recycle()
        }

        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateTitle()
                if (fromUser) {
                    onSeekBarChangeListener?.invoke(seekBar, progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    private fun updateTitle() {
        mTitleTextView.text = context.getString(R.string.custom_seekbar_title, title, getProgress())
    }

    fun getProgress(): Int = min + mSeekBar.progress

    fun setOnSeekBarChangeListener(listener: (seekBar: SeekBar?, progress: Int) -> Unit) {
        this.onSeekBarChangeListener = listener
    }

    fun increaseProgressBy(i: Int) {
        mSeekBar.progress += i
    }
}