package com.asmatullah.spaceapp.common.core.db.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.asmatullah.spaceapp.common.core.db.models.Station

@Dao
interface StationDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(station: Station)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(stations: List<Station>)

    @Query("SELECT * FROM station")
    suspend fun getStations(): List<Station>

    @Query("DELETE FROM station")
    suspend fun clear()
}