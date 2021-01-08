package com.asmatullah.spaceapp.home.ui.favorites

import com.asmatullah.spaceapp.common.core.db.models.Station
import com.asmatullah.spaceapp.common.core.ui.mvvm.BaseMvvmFragment
import com.asmatullah.spaceapp.common.core.ui.util.observeNonNull
import com.asmatullah.spaceapp.home.R
import com.asmatullah.spaceapp.home.databinding.FragmentFavoritesBinding
import com.asmatullah.spaceapp.home.ui.favorites.adapter.FavoritesAdapter
import kotlinx.android.synthetic.main.fragment_favorites.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment :
    BaseMvvmFragment<FavoritesViewModel, FragmentFavoritesBinding>(R.layout.fragment_favorites) {

    override val viewModel: FavoritesViewModel by viewModel()

    private val favoritesAdapter by lazy { FavoritesAdapter(viewModel::onClickFav) }

    override fun configureUI() {
        onActivityBackPressed()
        recView.adapter = favoritesAdapter
        setupViewModel()
    }

    private fun setupViewModel() {
        super.setupViewModel(viewModel)
        viewModel.favorites.observeNonNull(this) {
            favoritesAdapter.setItems(it)
        }
    }
}