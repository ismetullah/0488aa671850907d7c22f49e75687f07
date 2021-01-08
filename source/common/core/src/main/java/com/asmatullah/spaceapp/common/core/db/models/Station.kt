package com.asmatullah.spaceapp.common.core.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "station")
data class Station(
    @PrimaryKey
    val name: String,
    val coordinateX: Double,
    val coordinateY: Double,
    val capacity: Int,
    val need: Int,
    val stock: Int
)