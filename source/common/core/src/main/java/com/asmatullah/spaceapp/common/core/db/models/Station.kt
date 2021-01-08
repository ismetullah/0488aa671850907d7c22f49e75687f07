package com.asmatullah.spaceapp.common.core.db.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
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
    var isFav: Boolean = false,
    @ColumnInfo(name = "isCurrent")
    var isCurrent: Boolean = false
) : Parcelable