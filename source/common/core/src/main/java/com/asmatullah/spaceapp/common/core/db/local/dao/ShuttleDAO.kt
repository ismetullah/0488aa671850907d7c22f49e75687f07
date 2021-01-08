package com.asmatullah.spaceapp.common.core.db.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.asmatullah.spaceapp.common.core.db.models.Shuttle

@Dao
abstract class ShuttleDAO {
    @Transaction
    open suspend fun deleteAndInsert(shuttle: Shuttle) {
        clear()
        insert(shuttle)
    }

    @Insert
    abstract suspend fun insert(shuttle: Shuttle)

    @Query("SELECT * FROM shuttle LIMIT 1")
    abstract fun getShuttle(): LiveData<Shuttle>

    @Query("DELETE FROM shuttle")
    abstract suspend fun clear()
}