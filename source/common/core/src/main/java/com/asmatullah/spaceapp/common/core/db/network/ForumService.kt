package com.asmatullah.spaceapp.common.core.db.network

import com.asmatullah.spaceapp.common.core.db.models.Station
import retrofit2.http.GET

interface ForumService {
    @GET("e7211664-cbb6-4357-9c9d-f12bf8bab2e2")
    suspend fun getStations(): List<Station>
}