package com.asmatullah.spaceapp.common.core.ui.util

import android.app.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class DialogLifecycleObserver(
    private val lifecycleOwner: LifecycleOwner,
    private val dialog: Dialog
) : LifecycleEventObserver {
    init {
        lifecycleOwner.lifecycle.also { lifecycle ->
            lifecycle.addObserver(this)
            dialog.setOnDismissListener { lifecycle.removeObserver(this) }
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event > Lifecycle.Event.ON_RESUME) {
            dialog.dismiss()
            lifecycleOwner.lifecycle.removeObserver(this)
        }
    }
}