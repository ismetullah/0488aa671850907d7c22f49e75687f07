package com.asmatullah.spaceapp.common.core.ui.adapter

import androidx.recyclerview.widget.DiffUtil

abstract class BaseDiffUtil<T>(
    val oldList: List<T>,
    val newList: List<T>
) : DiffUtil.Callback() {

    override fun getNewListSize(): Int = newList.size

    override fun getOldListSize(): Int = oldList.size
}