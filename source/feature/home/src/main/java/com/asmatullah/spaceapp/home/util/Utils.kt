package com.asmatullah.spaceapp.home.util

import com.asmatullah.spaceapp.common.core.db.models.Shuttle
import com.asmatullah.spaceapp.common.core.db.models.Station
import com.asmatullah.spaceapp.common.core.util.Constants.DS_MULTIPLIER
import com.asmatullah.spaceapp.common.core.util.Constants.EUS_MULTIPLIER
import com.asmatullah.spaceapp.common.core.util.Constants.UGS_MULTIPLIER
import java.text.DecimalFormat
import kotlin.math.pow
import kotlin.math.sqrt

private val df = DecimalFormat("#.#")
fun formatDouble(d: Double): String {
    return df.format(d)
}

fun calculateEUS(station1: Station, station2: Station): Double {
    return sqrt(
        (station1.coordinateX - station2.coordinateX).pow(2) +
                (station1.coordinateY - station2.coordinateY).pow(2)
    )
}

fun calculateInitialUGS(shuttle: Shuttle) = shuttle.capacity * UGS_MULTIPLIER

fun calculateInitialEUS(shuttle: Shuttle) = shuttle.speed * EUS_MULTIPLIER

fun calculateInitialDS(shuttle: Shuttle) = shuttle.strength * DS_MULTIPLIER
