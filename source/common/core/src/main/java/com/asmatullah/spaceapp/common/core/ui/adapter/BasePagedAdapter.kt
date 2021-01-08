package com.asmatullah.spaceapp.common.core.ui.adapter

import android.view.ViewGroup

abstract class BasePagedAdapter<T> : BaseAdapter<T>() {
    var isLoading: Boolean = true
        set(value) {
            val oldValue = field
            field = value
            if (!oldValue && value) {
                notifyItemInserted(itemCount)
            } else {
                notifyItemRemoved(itemCount)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        return when (viewType) {
            1 -> LoadingViewHolder(parent)
            else -> getViewHolder(parent, viewType)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading && itemCount - 1 == position) 1 else super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (isLoading) 1 else 0
    }

    abstract fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T>
}