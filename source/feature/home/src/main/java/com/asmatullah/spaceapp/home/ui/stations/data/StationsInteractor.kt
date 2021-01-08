package com.asmatullah.spaceapp.home.ui.stations.data

import com.asmatullah.spaceapp.common.core.db.models.Station
import com.asmatullah.spaceapp.home.ui.stations.StationsContract

class StationsInteractor(private val repo: StationsContract.Repo) : StationsContract.Interactor {
    override suspend fun deleteStations() = repo.deleteStations()

    override suspend fun loadStationsFromServer() = repo.loadStationsFromServer()

    override suspend fun loadStations(): List<Station> {
        val stationsLiveData = repo.loadStationsFromDatabase()
        if (stationsLiveData.isNullOrEmpty()) {
            val list = repo.loadStationsFromServer()
            repo.updateStations(list)
        }
        return repo.loadStationsFromDatabase()
    }

    override suspend fun updateStation(station: Station) = repo.updateStation(station)

    override fun loadShuttle() = repo.loadShuttle()
}