package com.asmatullah.spaceapp.home.ui.stations

import androidx.recyclerview.widget.LinearSnapHelper
import com.asmatullah.spaceapp.common.core.db.models.Station
import com.asmatullah.spaceapp.common.core.ui.mvvm.BaseMvvmFragment
import com.asmatullah.spaceapp.common.core.ui.util.SimpleTextWatcher
import com.asmatullah.spaceapp.common.core.ui.util.observeNonNull
import com.asmatullah.spaceapp.common.core.util.DelayUtil
import com.asmatullah.spaceapp.home.R
import com.asmatullah.spaceapp.home.databinding.FragmentStationsBinding
import com.asmatullah.spaceapp.home.ui.stations.adapter.SearchAdapter
import com.asmatullah.spaceapp.home.ui.stations.adapter.StationsAdapter
import kotlinx.android.synthetic.main.fragment_stations.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class StationsFragment :
    BaseMvvmFragment<StationsViewModel, FragmentStationsBinding>(R.layout.fragment_stations) {

    override val viewModel: StationsViewModel by viewModel()

    private val stationsAdapter by lazy {
        StationsAdapter(
            viewModel::onClickTravel,
            viewModel::onClickFav
        )
    }
    private val searchAdapter by lazy { SearchAdapter(layoutInflater, this::onClickSearch) }

    override fun configureUI() {
        onActivityBackPressed()
        initRecView()
        initSearch()
        setupViewModel()
    }

    private fun initSearch() {
        searchBar.setCustomSuggestionAdapter(searchAdapter)
        searchBar.addTextChangeListener(object : SimpleTextWatcher() {
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                searchAdapter.filter.filter(text)
            }
        })
    }

    private fun initRecView() {
        recView.adapter = stationsAdapter
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recView)
        recView.post {
            // Set the width 4/5 of the whole screen.
            stationsAdapter.itemWidth = (recView.width * 4) / 5
        }
    }

    private fun setupViewModel() {
        super.setupViewModel(viewModel)
        viewModel.shuttle.observeNonNull(this) {
            viewModel.updateVariables(it)
        }
        viewModel.stations.observeNonNull(this) {
            if (viewModel.currentStation.value == null)
                viewModel.initCurrentStation()
            viewModel.sortStations()
        }
        viewModel.sortedStations.observeNonNull(this) {
            stationsAdapter.setItems(it)
            searchAdapter.suggestions = it
            if (viewModel.shouldScroll) {
                scrollTo(viewModel.currentStation.value)
                viewModel.shouldScroll = false
            }
        }
        viewModel.currentStation.observeNonNull(this) {
            stationsAdapter.currentStation = it
        }
        viewModel.gameOver.observeNonNull(this) {
            stationsAdapter.gameOver = it
            if (it) {
                onError(R.string.warning_game_over)
            }
        }
        viewModel.initialize()
    }

    private fun scrollTo(station: Station?) {
        if (station == null) return
        val pos = stationsAdapter.getItemPosition(station)
        if (pos != -1) {
            recView.smoothScrollToPosition(pos)
        }
    }

    private fun onClickSearch(station: Station) {
        searchBar.closeSearch()
        // Scroll to position after some seconds
        DelayUtil.postDelayedInUIThread({ scrollTo(station) }, 1_000)
    }
}