package com.asmatullah.spaceapp.common.core.ui.mvvm

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

internal class LifecycleCoroutineScope(
    lifecycleOwner: LifecycleOwner,
    dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : CoroutineScope, LifecycleObserver {

    override val coroutineContext: CoroutineContext = SupervisorJob() + dispatcher
    private val lifecycle = lifecycleOwner.lifecycle

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroyed() {
        coroutineContext.cancel()
        lifecycle.removeObserver(this)
    }
}