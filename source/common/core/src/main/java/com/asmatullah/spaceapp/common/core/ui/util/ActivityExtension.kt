package com.asmatullah.spaceapp.common.core.ui.util

import android.app.Activity
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

inline fun <reified T> AppCompatActivity.onNewIntent(): Intent {
	return Intent(this, T::class.java)
}

inline fun <reified T> Context.onNewIntent(): Intent {
	return Intent(this, T::class.java)
}

inline fun <reified T> Context.launchActivity() {
	this.startActivity(Intent(this, T::class.java))
}

inline fun <reified T> AppCompatActivity.launchActivity() {
	startActivity(Intent(this, T::class.java))
}

inline fun <reified T> AppCompatActivity.bringActivityToFront() {
	startActivity(Intent(this, T::class.java).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
}

inline fun <reified T> AppCompatActivity.launchActivityResult(requestCode: Int) {
	startActivityForResult(Intent(this, T::class.java), requestCode)
}

inline fun <reified T> AppCompatActivity.cleanLaunchActivity() {
	finish()
	startActivity(
		Intent(
			this, T::class.java
		).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
	)
}

inline fun <reified T> Context.cleanLaunchActivity() {
	startActivity(
		Intent(
			this, T::class.java
		).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
	)
}


inline fun <reified T> AppCompatActivity.cleanIntent(): Intent {
	finish()
	return Intent(
		this, T::class.java
	).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
}

inline fun <reified T> Activity.startJobService() {
	val name = ComponentName(this, T::class.java)
	val info =
		JobInfo.Builder(0, name).setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED).build()
	val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
	scheduler.schedule(info)
}

fun Activity.makeStatusBarTransparent(isDark: Boolean = false) {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
		window.apply {
			clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
			addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				decorView.systemUiVisibility =
					View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or if (!isDark) View.SYSTEM_UI_FLAG_LAYOUT_STABLE else View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
			} else {
				decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
			}
			statusBarColor = Color.TRANSPARENT
		}
	}
}

fun Activity.getWidthScreen(): Int {
	val display = windowManager.defaultDisplay
	val size = Point()
	display.getSize(size)
	return size.x
}

fun Context.getScreenWidth(): Int {
	return getApplicationContext().getResources().getDisplayMetrics().widthPixels;
}

fun Context.getScreenHeight(): Int {
	return getApplicationContext().getResources().getDisplayMetrics().heightPixels;
}

fun Context.getStatusBarHeight(): Int {
	val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
	if (resourceId > 0) {
		return resources.getDimensionPixelSize(resourceId)
	}
	return 0
}

fun Context.getActivity(): Activity? {
	var context = this
	while (context is ContextWrapper) {
		if (context is Activity) {
			return context
		}
		context = context.baseContext
	}
	return null
}

fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, observer: (data: T) -> Unit) {
	this.observe(owner, Observer {
		if (it != null) {
			observer.invoke(it)
		}
	})
}

internal fun Context.getColorCompat(
	@ColorRes
	color: Int
) = ContextCompat.getColor(this, color)

internal fun TextView.setTextColorRes(
	@ColorRes
	color: Int
) = setTextColor(context.getColorCompat(color))

fun Boolean?.toInt(): Int {
	return if (this == true) 1 else 0
}

fun Context.showKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun Context.closeKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}