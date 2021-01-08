package com.asmatullah.spaceapp.home.ui.favorites.data

import com.asmatullah.spaceapp.common.core.db.models.Station
import com.asmatullah.spaceapp.home.ui.favorites.FavoritesContract

class FavoritesInteractor(private val repo: FavoritesContract.Repo) : FavoritesContract.Interactor {
    override fun getFavorites() = repo.getFavorites()
    override suspend fun updateStation(station: Station) = repo.updateStation(station)
}