package com.asmatullah.spaceapp.home.ui.stations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.asmatullah.spaceapp.common.core.db.models.Shuttle
import com.asmatullah.spaceapp.common.core.db.models.Station

interface StationsContract {
    interface ViewModel {
        var stations: MutableLiveData<ArrayList<Station>>
        var currentStation: MutableLiveData<Station>
        var shuttle: LiveData<Shuttle>
        val UGS: MutableLiveData<Int>
        val EUS: MutableLiveData<Double>
        val DS: MutableLiveData<Int>
        val timeleft: MutableLiveData<Int>

        fun onClickTravel(station: Station)
        fun onClickFav(station: Station)
    }

    interface Repo {
        suspend fun deleteStations()
        suspend fun loadStationsFromServer(): ArrayList<Station>
        suspend fun loadStationsFromDatabase(): List<Station>
        suspend fun updateStations(list: ArrayList<Station>)
        suspend fun updateStation(station: Station)
        fun loadShuttle(): LiveData<Shuttle>
    }

    interface Interactor {
        suspend fun deleteStations()
        suspend fun loadStationsFromServer(): ArrayList<Station>
        suspend fun loadStations(): List<Station>
        suspend fun updateStation(station: Station)
        fun loadShuttle(): LiveData<Shuttle>
    }
}