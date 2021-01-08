package com.asmatullah.spaceapp.common.core.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.asmatullah.spaceapp.common.core.R
import com.asmatullah.spaceapp.common.core.util.DelayUtil
import com.asmatullah.spaceapp.common.uikit.common.showIf
import kotlinx.android.synthetic.main.view_warning_dialog.*

class WarningDialog(context: Context) : Dialog(context, R.style.AppTheme_WarningDialog) {
    companion object {
        const val DURATION_LONG: Long = 6000
        const val DURATION_SHORT: Long = 3000
    }

    private var view: Int = R.layout.view_warning_dialog

    private var message: CharSequence? = ""

    private var mIsCancelable: Boolean = true

    private var duration = DURATION_SHORT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(mIsCancelable)
        setCanceledOnTouchOutside(mIsCancelable)
        setContentView(view)

        setupMessage()
    }

    override fun onStart() {
        super.onStart()
        DelayUtil.postDelayedInUIThread({ dismiss() }, duration)
    }

    private fun setupMessage() {
        descriptionView.showIf(!message.isNullOrEmpty())
        descriptionView.text = message
    }

    fun isCancelable(boolean: Boolean): WarningDialog {
        mIsCancelable = boolean
        return this
    }

    fun setMessage(text: CharSequence?): WarningDialog {
        message = text
        return this
    }

    fun setDuration(duration: Long): WarningDialog {
        this.duration = duration
        return this
    }
}