package com.asmatullah.spaceapp.home.ui.stations.adapter

import android.view.ViewGroup
import com.asmatullah.spaceapp.common.core.db.models.Station
import com.asmatullah.spaceapp.common.core.ui.adapter.BaseAdapter
import com.asmatullah.spaceapp.common.core.ui.adapter.BaseViewHolder
import com.asmatullah.spaceapp.common.core.ui.util.setThrottleOnClickListener
import com.asmatullah.spaceapp.common.uikit.common.showIf
import com.asmatullah.spaceapp.home.R
import com.asmatullah.spaceapp.home.util.calculateEUS
import com.asmatullah.spaceapp.home.util.formatDouble
import kotlinx.android.synthetic.main.item_station.view.*

class StationsAdapter(
    private val onClickTravel: (Station) -> Unit,
    private val onClickFav: (Station) -> Unit
) : BaseAdapter<Station>() {
    var gameOver = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var currentStation: Station = Station("", 0.0, 0.0, 0, 0, 0)
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var itemWidth: Int? = null

    override fun setItems(_items: List<Station>?) =
        super.setItems(StationsDiff(data, _items ?: arrayListOf()))

    override fun getItemPosition(item: Station): Int {
        data.forEachIndexed { i, answer ->
            if (answer.name == item.name)
                return i
        }
        return -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    inner class ViewHolder(parent: ViewGroup) :
        BaseViewHolder<Station>(parent, R.layout.item_station) {

        override fun onBind(item: Station) = with(itemView) {
            super.onBind(item)
            itemWidth?.let { w ->
                layoutParams.width = w
                requestLayout()
            }

            root.setBackgroundResource(getBackgroundResource(item))

            val eus = calculateEUS(item, currentStation)
            tvName.text = item.name
            tvDetails.text = context.getString(
                R.string.format_station_details,
                item.stock,
                item.capacity,
                formatDouble(eus)
            )

            btnFav.setImageResource(if (item.isFav) R.drawable.ic_star_filled else R.drawable.ic_star_empty)
            btnFav.setThrottleOnClickListener {
                onClickFav.invoke(item)
                btnFav.setImageResource(if (item.isFav) R.drawable.ic_star_empty else R.drawable.ic_star_filled)
            }

            btnTravel.showIf(shouldShowTravelBtn(item))
            btnTravel.setThrottleOnClickListener {
                onClickTravel.invoke(item)
            }
        }

        private fun getBackgroundResource(item: Station) = when {
            item.name == currentStation.name -> R.drawable.bg_station_current
            item.need == 0 -> R.drawable.bg_station_disabled
            else -> R.drawable.bg_station
        }

        private fun shouldShowTravelBtn(item: Station): Boolean {
            return !gameOver && (item.need != 0) && (item.name != currentStation.name)
        }
    }
}