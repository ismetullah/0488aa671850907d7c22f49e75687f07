package com.asmatullah.spaceapp.home.ui.favorites.data

import com.asmatullah.spaceapp.common.core.db.local.dao.StationDAO
import com.asmatullah.spaceapp.common.core.db.models.Station
import com.asmatullah.spaceapp.home.ui.favorites.FavoritesContract

class FavoritesRepo(private val stationDAO: StationDAO) : FavoritesContract.Repo {
    override fun getFavorites() = stationDAO.getFavorites()
    override suspend fun updateStation(station: Station) = stationDAO.insert(station)
}