package com.asmatullah.spaceapp.common.core.db.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.asmatullah.spaceapp.common.core.db.models.Station

@Dao
abstract class StationDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(station: Station)

    @Update
    abstract suspend fun update(station: Station)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(stations: List<Station>)

    @Query("SELECT * FROM station")
    abstract suspend fun getStations(): List<Station>

    @Query("SELECT * FROM station")
    abstract fun getStationsLive(): LiveData<List<Station>>

    @Query("DELETE FROM station")
    abstract suspend fun clear()

    @Query("SELECT * FROM station WHERE station.isFav")
    abstract fun getFavorites(): LiveData<List<Station>>

    @Query("SELECT * FROM station WHERE station.isCurrent")
    abstract fun getCurrentStationLive(): LiveData<Station>

    @Query("SELECT * FROM station WHERE station.isCurrent")
    abstract suspend fun getCurrentStation(): Station?

    @Transaction
    open suspend fun updateCurrentStation(station: Station) {
        val oldStation = getCurrentStation()
        if (oldStation != null) {
            oldStation.isCurrent = false
            insert(oldStation)
        }
        station.isCurrent = true
        insert(station)
    }
}