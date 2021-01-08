package com.asmatullah.spaceapp.common.core.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import com.asmatullah.spaceapp.common.core.ui.util.setThrottleOnClickListener

abstract class BaseViewHolder<T>(
    view: View,
    private val listener: ((T) -> Unit)?
) : RecyclerView.ViewHolder(view), LifecycleOwner {

    constructor(parent: ViewGroup, @LayoutRes layoutId: Int, listener: ((T) -> Unit)? = null) :
            this(LayoutInflater.from(parent.context).inflate(layoutId, parent, false), listener) {
        lifecycleRegistry.markState(Lifecycle.State.INITIALIZED)
    }

    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

    override fun getLifecycle() = lifecycleRegistry

    open fun onBind(item: T) {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        itemView.setThrottleOnClickListener {
            listener?.invoke(item)
            onItemClick(item)
        }
    }

    protected open fun onItemClick(item: T) = Unit

    open fun onViewRecycled() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    open fun onViewAttachedToWindow() = Unit
    open fun onViewDetachedFromWindow() = Unit
}