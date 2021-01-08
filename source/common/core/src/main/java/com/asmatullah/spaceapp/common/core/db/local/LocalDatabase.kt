package com.asmatullah.spaceapp.common.core.db.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.asmatullah.spaceapp.common.core.db.local.dao.ShuttleDAO
import com.asmatullah.spaceapp.common.core.db.local.dao.StationDAO
import com.asmatullah.spaceapp.common.core.db.models.Shuttle
import com.asmatullah.spaceapp.common.core.db.models.Station

@Database(
    entities = [Shuttle::class, Station::class],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun shuttleDAO() : ShuttleDAO
    abstract fun stationDAO() : StationDAO

    companion object {
        private const val DATABASE_NAME = "LOCAL_DATABASE"

        fun initDatabase(context: Context): LocalDatabase {
            return Room
                .databaseBuilder(context, LocalDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}