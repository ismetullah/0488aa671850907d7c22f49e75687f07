package com.asmatullah.spaceapp.common.core.db.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.asmatullah.spaceapp.common.core.db.models.Shuttle

@Dao
interface ShuttleDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shuttle: Shuttle)

    @Query("SELECT * FROM shuttle LIMIT 1")
    fun getShuttle(): LiveData<Shuttle>
}