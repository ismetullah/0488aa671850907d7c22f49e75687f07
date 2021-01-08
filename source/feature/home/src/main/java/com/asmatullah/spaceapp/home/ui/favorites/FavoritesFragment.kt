package com.asmatullah.spaceapp.home.ui.favorites

import com.asmatullah.spaceapp.common.core.ui.mvvm.BaseMvvmFragment
import com.asmatullah.spaceapp.home.R
import com.asmatullah.spaceapp.home.databinding.FragmentFavoritesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment :
    BaseMvvmFragment<FavoritesViewModel, FragmentFavoritesBinding>(R.layout.fragment_favorites) {

    override val viewModel: FavoritesViewModel by viewModel()

    override fun configureUI() {
        onActivityBackPressed()
        setupViewModel()
    }

    private fun setupViewModel() {
        super.setupViewModel(viewModel)
    }
}