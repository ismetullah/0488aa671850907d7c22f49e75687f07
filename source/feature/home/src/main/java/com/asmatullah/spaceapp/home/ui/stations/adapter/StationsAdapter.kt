package com.asmatullah.spaceapp.home.ui.stations.adapter

import android.view.ViewGroup
import com.asmatullah.spaceapp.common.core.db.models.Station
import com.asmatullah.spaceapp.common.core.ui.adapter.BaseAdapter
import com.asmatullah.spaceapp.common.core.ui.adapter.BaseViewHolder
import com.asmatullah.spaceapp.home.R

class StationsAdapter : BaseAdapter<Station>() {
    private var bannerWidth: Int? = null

    fun setBannerWidth(width: Int) {
        this.bannerWidth = width
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MainViewHolder(parent)

    inner class MainViewHolder(parent: ViewGroup) :
        BaseViewHolder<Station>(parent, R.layout.item_station) {

        override fun onBind(item: Station) = with(itemView) {
            super.onBind(item)
        }
    }
}