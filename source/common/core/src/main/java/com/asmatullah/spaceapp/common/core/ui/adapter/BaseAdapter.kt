package com.asmatullah.spaceapp.common.core.ui.adapter

import androidx.recyclerview.widget.DiffUtil

abstract class BaseAdapter<T> :
    androidx.recyclerview.widget.RecyclerView.Adapter<BaseViewHolder<T>>() {

    protected val data = mutableListOf<T>()

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onViewRecycled(holder: BaseViewHolder<T>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    open fun getItem(position: Int): T = safeGet(data, position)

    open fun getItemPosition(item: T): Int {
        data.forEachIndexed { i, answer ->
            if (answer == item)
                return i
        }
        return -1
    }

    protected fun safeGet(list: List<T>, position: Int): T {
        return if (position >= list.size) {
            val index = list.size - 1
            if (index < 0)
                list[0]
            else list[index]
        } else {
            list[position]
        }
    }

    fun getItemRange(startPosition: Int, endPosition: Int) =
        data.subList(startPosition, endPosition)

    fun isEmpty() = data.size == 0

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    open fun setItems(_items: List<T>?) {
        val items = _items ?: arrayListOf()
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    open fun setItems(diffUtil: BaseDiffUtil<T>) {
        val diffCalculator = DiffUtil.calculateDiff(diffUtil)
        diffCalculator.dispatchUpdatesTo(this)
        data.clear()
        data.addAll(diffUtil.newList)
    }

    fun removeItem(element: T) {
        val position = data.indexOf(element)
        if (position != -1) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun removeItem(predicate: (T) -> Boolean) {
        val position = data.indexOfFirst(predicate)
        if (position != -1) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}