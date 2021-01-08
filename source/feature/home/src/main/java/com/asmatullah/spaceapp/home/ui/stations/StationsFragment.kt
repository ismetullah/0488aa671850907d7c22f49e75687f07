package com.asmatullah.spaceapp.home.ui.stations

import com.asmatullah.spaceapp.common.core.ui.mvvm.BaseMvvmFragment
import com.asmatullah.spaceapp.common.core.ui.util.observeNonNull
import com.asmatullah.spaceapp.home.R
import com.asmatullah.spaceapp.home.databinding.FragmentStationsBinding
import com.asmatullah.spaceapp.home.ui.stations.adapter.StationsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class StationsFragment :
    BaseMvvmFragment<StationsViewModel, FragmentStationsBinding>(R.layout.fragment_stations) {

    override val viewModel: StationsViewModel by viewModel()

    private val stationsAdapter by lazy { StationsAdapter() }

    override fun configureUI() {
        onActivityBackPressed()
        setupViewModel()
    }

    private fun setupViewModel() {
        super.setupViewModel(viewModel)
        viewModel.stations.observeNonNull(this) {

        }
        viewModel.initialize()
    }
}