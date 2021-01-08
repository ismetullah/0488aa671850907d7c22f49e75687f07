package com.asmatullah.spaceapp.common.core.db.network

import com.asmatullah.spaceapp.common.core.db.models.Station

interface NetworkService {
    suspend fun getStations(): List<Station>
}

class NetworkServiceImpl(
    private val service: ForumService
) : NetworkService {
    override suspend fun getStations(): List<Station> = service.getStations()
}