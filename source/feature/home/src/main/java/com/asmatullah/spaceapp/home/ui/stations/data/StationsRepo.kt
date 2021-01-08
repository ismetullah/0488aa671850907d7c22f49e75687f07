package com.asmatullah.spaceapp.home.ui.stations.data

import androidx.lifecycle.LiveData
import com.asmatullah.spaceapp.common.core.db.local.dao.ShuttleDAO
import com.asmatullah.spaceapp.common.core.db.local.dao.StationDAO
import com.asmatullah.spaceapp.common.core.db.models.Station
import com.asmatullah.spaceapp.common.core.db.network.NetworkService
import com.asmatullah.spaceapp.home.ui.stations.StationsContract

class StationsRepo(
    private val shuttleDao: ShuttleDAO,
    private val stationDAO: StationDAO,
    private val network: NetworkService
) : StationsContract.Repo {
    override suspend fun loadStationsFromServer(): List<Station> = network.getStations()

    override suspend fun loadStationsFromDatabase(): LiveData<List<Station>> {
        TODO("Not yet implemented")
    }

    override fun loadShuttle() = shuttleDao.getShuttle()
}