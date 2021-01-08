package com.asmatullah.spaceapp.home.ui.favorites

import com.asmatullah.spaceapp.common.core.db.models.Station
import com.asmatullah.spaceapp.common.core.ui.mvvm.BaseViewModel

class FavoritesViewModel(private val repo: FavoritesContract.Repo) : BaseViewModel(),
    FavoritesContract.ViewModel {
    override val favorites = repo.getFavorites()

    fun onClickFav(station: Station) {
        handleRequest {
            station.isFav = false
            repo.updateStation(station)
        }
    }
}