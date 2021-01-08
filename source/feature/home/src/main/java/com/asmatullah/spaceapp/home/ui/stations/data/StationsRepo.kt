package com.asmatullah.spaceapp.home.ui.stations.data

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

    override suspend fun deleteStations() = stationDAO.clear()

    override suspend fun loadStationsFromServer(): ArrayList<Station> = network.getStations()

    override suspend fun loadStationsFromDatabase() = stationDAO.getStations()

    override fun loadStationsFromDatabaseLive() = stationDAO.getStationsLive()

    override suspend fun updateStations(list: ArrayList<Station>) = stationDAO.update(list)

    override suspend fun updateStation(station: Station) = stationDAO.insert(station)

    override fun loadCurrentStation() = stationDAO.getCurrentStationLive()

    override suspend fun loadCurrentStationL() = stationDAO.getCurrentStation()

    override suspend fun updateCurrentStation(station: Station) = stationDAO.updateCurrentStation(station)

    override fun loadShuttle() = shuttleDao.getShuttle()
}