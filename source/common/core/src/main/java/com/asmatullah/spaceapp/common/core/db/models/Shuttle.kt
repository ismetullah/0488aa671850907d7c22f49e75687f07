package com.asmatullah.spaceapp.common.core.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shuttle")
data class Shuttle(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "strength")
    var strength: Int,
    @ColumnInfo(name = "capacity")
    var capacity: Int,
    @ColumnInfo(name = "speed")
    var speed: Int,
    @ColumnInfo(name = "damageCapacity")
    var damageCapacity: Int = 100,
)