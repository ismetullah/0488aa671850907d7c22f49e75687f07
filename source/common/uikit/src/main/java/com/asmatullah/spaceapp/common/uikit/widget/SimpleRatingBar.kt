package com.asmatullah.spaceapp.common.uikit.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.asmatullah.spaceapp.common.uikit.R

class SimpleRatingBar : View, View.OnTouchListener {

    companion object {
        private const val IS_INDICATOR_DEFAULT = false
        private const val RATING_DEFAULT = 2.5f
        private const val RATING_COUNT_DEFAULT = 5
        private const val RATING_SIZE_DEFAULT = 48
        private const val RATING_MARGIN_DEFAULT = 10
    }

    var isIndicator: Boolean = false
        set(value) {
            field = value
            setOnTouchListener(if (isIndicator) null else this)
        }
    var rating: Float = 0f
        set(value) {
            var newRating = value
            if (newRating < 0) {
                newRating = 0f
            } else if (newRating > ratingCount) {
                newRating = ratingCount.toFloat()
            }
            field = value
            onRatingChangedListener?.onRatingChange(rating, newRating)
            invalidate()
        }
    var onRatingChangedListener: OnRatingChangedListener? = null
    private var bitmapEmpty: Bitmap? = null
    private var bitmapHalf: Bitmap? = null
    private var bitmapFilled: Bitmap? = null
    private val rect = Rect()
    var ratingCount: Int = 5
        set(value) {
            field = value
            requestLayout()
        }
    var ratingSize: Int = 0
        set(value) {
            field = value
            requestLayout()
        }
    var ratingMargin: Int = 0
        set(value) {
            field = value
            requestLayout()
        }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleRatingBar)
        isIndicator = typedArray.getBoolean(
            R.styleable.SimpleRatingBar_isIndicator,
            IS_INDICATOR_DEFAULT
        )
        rating = typedArray.getFloat(
            R.styleable.SimpleRatingBar_rating,
            RATING_DEFAULT
        )
        ratingCount = typedArray.getInteger(
            R.styleable.SimpleRatingBar_ratingCount,
            RATING_COUNT_DEFAULT
        )
        bitmapEmpty = AppCompatResources.getDrawable(
            context, typedArray.getResourceId(
                R.styleable.SimpleRatingBar_ratingEmpty,
                R.drawable.ic_star_off
            )
        )?.toBitmap()
        bitmapHalf = AppCompatResources.getDrawable(
            context, typedArray.getResourceId(
                R.styleable.SimpleRatingBar_ratingHalf,
                R.drawable.ic_star_half
            )
        )?.toBitmap()
        bitmapFilled = AppCompatResources.getDrawable(
            context, typedArray.getResourceId(
                R.styleable.SimpleRatingBar_ratingFilled,
                R.drawable.ic_star_on
            )
        )?.toBitmap()
        ratingSize = typedArray.getDimension(
            R.styleable.SimpleRatingBar_ratingSize,
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                RATING_SIZE_DEFAULT.toFloat(),
                resources.displayMetrics
            )
        ).toInt()
        ratingMargin = typedArray.getDimension(
            R.styleable.SimpleRatingBar_ratingMargin,
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                RATING_MARGIN_DEFAULT.toFloat(),
                resources.displayMetrics
            )
        ).toInt()

        if (ratingSize < 0) {
            throw IllegalArgumentException("Rating size < 0 is not possible")
        }
        if (ratingCount < 1) {
            throw IllegalArgumentException("Rating count < 1 is not possible")
        }
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            resolveSize(
                ratingSize * ratingCount + ratingMargin * (ratingCount - 1),
                widthMeasureSpec
            ), resolveSize(ratingSize, heightMeasureSpec)
        )
    }

    override fun onDraw(canvas: Canvas) {
        setOnTouchListener(if (isIndicator) null else this)
        if (bitmapEmpty != null && bitmapHalf != null && bitmapFilled != null) {
            rect.set(0, 0, ratingSize, ratingSize)
            var fullDrawablesCount = rating.toInt()
            val emptyDrawablesCount = ratingCount - Math.round(rating)
            val isDrawableHalf =
                rating - fullDrawablesCount >= 0.25f && rating - fullDrawablesCount < 0.75f

            if (rating - fullDrawablesCount >= 0.75f) {
                fullDrawablesCount++
            }
            drawBitmaps(fullDrawablesCount, canvas, isDrawableHalf, emptyDrawablesCount)
        }
    }

    private fun drawBitmaps(
        fullDrawablesCount: Int,
        canvas: Canvas,
        isDrawableHalf: Boolean,
        emptyDrawablesCount: Int
    ) {
        for (i in 0..fullDrawablesCount - 1) {
            drawRating(canvas, bitmapFilled as Bitmap)
        }
        if (isDrawableHalf) {
            drawRating(canvas, bitmapHalf as Bitmap)
        }
        for (i in 0..emptyDrawablesCount - 1) {
            drawRating(canvas, bitmapEmpty as Bitmap)
        }
    }

    private fun drawRating(canvas: Canvas, bitmap: Bitmap) {
        canvas.drawBitmap(bitmap, null, rect, null)
        rect.offset(ratingSize + ratingMargin, 0)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                rating = Math.round(event.x / width * ratingCount + 0.5).toFloat()
                return false
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onSaveInstanceState(): Parcelable? {
        if (super.onSaveInstanceState() == null)
            return super.onSaveInstanceState()

        val savedState =
            SavedState(
                super.onSaveInstanceState()!!
            )
        savedState.rating = rating
        savedState.isIndicator = isIndicator
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            rating = state.rating
            isIndicator = state.isIndicator
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    internal class SavedState : View.BaseSavedState {
        var rating: Float = 0f
        var isIndicator: Boolean = false

        constructor(superState: Parcelable) : super(superState)

        private constructor(source: Parcel) : super(source) {
            this.rating = source.readFloat()
            this.isIndicator = source.readInt() != 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeFloat(this.rating)
            dest.writeInt(if (this.isIndicator) 1 else 0)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(
                        `in`
                    )
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    interface OnRatingChangedListener {
        fun onRatingChange(oldRating: Float, newRating: Float)
    }
}