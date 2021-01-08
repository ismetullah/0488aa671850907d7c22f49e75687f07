package com.asmatullah.spaceapp.common.core.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "station")
data class Station(
    @PrimaryKey
    val name: String,
    var coordinateX: Double,
    var coordinateY: Double,
    var capacity: Int,
    var need: Int,
    var stock: Int,
    @ColumnInfo(name = "isFav")
    var isFav: Boolean = false
)