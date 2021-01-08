package com.asmatullah.spaceapp.home.ui.stations.adapter

import com.asmatullah.spaceapp.common.core.db.models.Station
import com.asmatullah.spaceapp.common.core.ui.adapter.BaseDiffUtil

class StationsDiff(
    oldList: List<Station>,
    newList: List<Station>
) : BaseDiffUtil<Station>(oldList, newList) {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.name == newItem.name &&
                oldItem.coordinateX == newItem.coordinateX &&
                oldItem.coordinateY == newItem.coordinateY &&
                oldItem.isFav == newItem.isFav &&
                oldItem.need == newItem.need &&
                oldItem.capacity == newItem.capacity &&
                oldItem.stock == newItem.stock
    }
}