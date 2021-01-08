package com.asmatullah.spaceapp.home.ui.favorites.adapter

import android.view.ViewGroup
import com.asmatullah.spaceapp.common.core.db.models.Station
import com.asmatullah.spaceapp.common.core.ui.adapter.BaseAdapter
import com.asmatullah.spaceapp.common.core.ui.adapter.BaseViewHolder
import com.asmatullah.spaceapp.home.R
import com.asmatullah.spaceapp.home.ui.stations.adapter.StationsDiff
import com.asmatullah.spaceapp.home.util.calculateEUS
import com.asmatullah.spaceapp.home.util.formatDouble
import kotlinx.android.synthetic.main.item_station.view.*

class FavoritesAdapter(
    private val onClickFav: (Station) -> Unit
) : BaseAdapter<Station>() {
    private val stationEarth = Station("", 0.0, 0.0, 0, 0, 0)

    override fun setItems(_items: List<Station>?) =
        super.setItems(StationsDiff(data, _items ?: arrayListOf()))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    inner class ViewHolder(parent: ViewGroup) :
        BaseViewHolder<Station>(parent, R.layout.item_favorite) {

        override fun onBind(item: Station) = with(itemView) {
            super.onBind(item)
            root.setBackgroundResource(getBackgroundResource(item))

            val eus = calculateEUS(item, stationEarth)
            tvName.text = item.name
            tvDetails.text = context.getString(
                R.string.format_favorite_details,
                item.stock,
                item.capacity,
                formatDouble(eus)
            )

            btnFav.setImageResource(if (item.isFav) R.drawable.ic_star_filled else R.drawable.ic_star_empty)
            btnFav.setOnClickListener {
                onClickFav.invoke(item)
            }
        }

        private fun getBackgroundResource(item: Station) = when {
            item.need == 0 -> R.drawable.bg_station_disabled
            else -> R.drawable.bg_station
        }
    }
}