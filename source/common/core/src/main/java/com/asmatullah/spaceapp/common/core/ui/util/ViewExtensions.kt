package com.asmatullah.spaceapp.common.core.ui.util

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.text.Editable
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.asmatullah.spaceapp.common.core.util.AnimatorListener
import com.google.android.material.textfield.TextInputLayout

private const val CLICK_DELAY_MILLIS = 500L

fun View.requireActivity(): AppCompatActivity {
    var context: Context = context
    while (context is ContextWrapper) {
        if (context is AppCompatActivity) {
            return context
        }
        context = context.baseContext
    }

    throw IllegalStateException("View is not attached to any activity")
}

fun View.setThrottleOnClickListener(callback: (view: View) -> Unit) {
    var lastClickTime = 0L

    this.setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()

        if (currentTimeMillis - lastClickTime > CLICK_DELAY_MILLIS) {
            lastClickTime = currentTimeMillis
            callback.invoke(it)
        }
    }
}

fun EditText.afterTextChanged(callback: (String) -> Unit) =
    this.addTextChangedListener(object : SimpleTextWatcher() {
        override fun afterTextChanged(text: Editable) {
            super.afterTextChanged(text)
            callback(text.toString())
        }
    })

inline fun EditText.setKeyActionListener(actionId: Int, crossinline callback: () -> Unit) {
    this.setOnEditorActionListener { _, targetActionId, _ ->
        return@setOnEditorActionListener if (targetActionId == actionId) {
            callback.invoke()
            true
        } else {
            false
        }
    }
}

fun View.focusAndShowKeyboard() {
    /**
     * This is to be called when the window already has focus.
     */
    fun View.showTheKeyboardNow() {
        if (isFocused) {
            post {
                // We still post the call, just in case we are being notified of the windows focus
                // but InputMethodManager didn't get properly setup yet.
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

    requestFocus()
    if (hasWindowFocus()) {
        // No need to wait for the window to get focus.
        showTheKeyboardNow()
    } else {
        // We need to wait until the window gets focus.
        viewTreeObserver.addOnWindowFocusChangeListener(
            object : ViewTreeObserver.OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(hasFocus: Boolean) {
                    // This notification will arrive just before the InputMethodManager gets set up.
                    if (hasFocus) {
                        this@focusAndShowKeyboard.showTheKeyboardNow()
                        // It’s very important to remove this listener once we are done.
                        viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                }
            })
    }
}

fun Activity.hideKeyboard() {
    currentFocus?.hideKeyboard()
}

fun View.hideKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(windowToken, 0)
}

fun Context.toast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}

fun Context.toast(@StringRes textRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, textRes, duration).show()
}

fun Fragment.toast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), text, duration).show()
}

fun Fragment.toast(@StringRes textRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), textRes, duration).show()
}

fun Context.dip(value: Int): Int = dipF(value).toInt()
fun Context.dipF(value: Int): Float = value * resources.displayMetrics.density

fun View.dip(value: Int): Int = context.dip(value)
fun View.dipF(value: Int): Float = context.dipF(value)

fun Fragment.dip(value: Int): Int = requireContext().dip(value)
fun Fragment.dipF(value: Int): Float = requireContext().dipF(value)

fun View.animateAlpha(alpha: Float, duration: Long = 250, visible: Boolean) {
    animate().alpha(alpha).setDuration(duration)
        .setListener(object : AnimatorListener() {
            override fun onAnimationEnd(animation: Animator?) {
                isVisible = visible
            }
        })
        .start()
}

fun EditText.setErrorText(errorText: String?) {
    val parentTextInputLayout = getParentTextInputLayout()
    if (parentTextInputLayout != null) {
        parentTextInputLayout.isErrorEnabled = !errorText.isNullOrEmpty();
        parentTextInputLayout.error = errorText;
    } else {
        error = errorText
    }
}

fun EditText.clearError() {
    val parentTextInputLayout = getParentTextInputLayout()
    if (parentTextInputLayout != null) {
        parentTextInputLayout.error = null
        parentTextInputLayout.isErrorEnabled = false
    } else if (error != null) {
        error = null
    }
}

fun EditText.getParentTextInputLayout(): TextInputLayout? {
    if (parent != null) {
        if (parent is TextInputLayout) {
            return parent as TextInputLayout
        }

        val parentParent = parent.parent
        if (parentParent != null && parentParent is TextInputLayout) {
            return parentParent
        }
    }
    return null
}