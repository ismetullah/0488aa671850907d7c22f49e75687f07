package com.asmatullah.spaceapp.home.ui.stations.data

import androidx.lifecycle.LiveData
import com.asmatullah.spaceapp.common.core.db.models.Station
import com.asmatullah.spaceapp.home.ui.stations.StationsContract

class StationsInteractor(private val repo: StationsContract.Repo) : StationsContract.Interactor {
    override suspend fun loadStationsFromServer(): List<Station> = repo.loadStationsFromServer()

    override suspend fun loadStationsFromDatabase(): LiveData<List<Station>> {
        TODO("Not yet implemented")
    }

    override fun loadShuttle() = repo.loadShuttle()
}