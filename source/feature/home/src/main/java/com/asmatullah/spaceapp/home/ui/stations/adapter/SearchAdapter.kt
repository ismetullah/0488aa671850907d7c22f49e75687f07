package com.asmatullah.spaceapp.home.ui.stations.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.asmatullah.spaceapp.common.core.db.models.Station
import com.asmatullah.spaceapp.common.core.ui.util.setThrottleOnClickListener
import com.asmatullah.spaceapp.common.uikit.common.showIf
import com.asmatullah.spaceapp.common.uikit.widget.searchbar.adapter.SuggestionsAdapter
import com.asmatullah.spaceapp.home.R
import kotlinx.android.synthetic.main.item_search.view.*

class SearchAdapter(inflater: LayoutInflater, private val onClickListener: (Station) -> Unit) :
    SuggestionsAdapter<Station, SearchAdapter.ViewHolder>(inflater) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = layoutInflater.inflate(R.layout.item_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindSuggestionHolder(item: Station, holder: ViewHolder, position: Int) {
        holder.onBind(item, position == itemCount - 1)
    }

    override fun getSingleViewHeight() = 50

    override fun getFilter() = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val results = FilterResults()
            val term = constraint.toString().toLowerCase().trim()
            if (term.isEmpty()) {
                suggestions = suggestions_clone
            } else {
                suggestions = ArrayList()
                suggestions_clone.forEach {
                    if (it.name.toLowerCase().contains(term))
                        suggestions.add(it)
                }
            }
            results.values = suggestions
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            suggestions = results.values as ArrayList<Station>
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(item: Station, isLastItem: Boolean) = with(itemView) {
            tvName.text = item.name
            divider.showIf(!isLastItem)
            setThrottleOnClickListener {
                onClickListener.invoke(item)
            }
        }
    }
}