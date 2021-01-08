package com.asmatullah.spaceapp.common.core.db.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.asmatullah.spaceapp.common.core.db.models.Station

@Dao
interface StationDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(station: Station)

    @Query("SELECT * FROM station")
    fun getStations(): LiveData<List<Station>>
}