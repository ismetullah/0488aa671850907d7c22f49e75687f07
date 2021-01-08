package com.asmatullah.spaceapp.home.ui.stations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.asmatullah.spaceapp.common.core.db.models.Shuttle
import com.asmatullah.spaceapp.common.core.db.models.Station

interface StationsContract {
    interface ViewModel {
        var sortedStations: MutableLiveData<List<Station>>
        var stations: LiveData<List<Station>>
        var currentStation: LiveData<Station>
        var shuttle: LiveData<Shuttle>
        val UGS: MutableLiveData<Int>
        val EUS: MutableLiveData<Double>
        val DS: MutableLiveData<Int>
        val timeleft: MutableLiveData<Int>
        val damageLeft: MutableLiveData<Int>
        val gameOver: MutableLiveData<Boolean>
        // Indicates whether the recview should scroll to the currentStation
        val shouldScroll: Boolean

        fun loadStations()
        fun initCurrentStation()
        fun sortStations()
        fun onClickTravel(station: Station)
        fun onClickFav(station: Station)
    }

    interface Repo {
        suspend fun deleteStations()
        suspend fun loadStationsFromServer(): ArrayList<Station>
        suspend fun loadStationsFromDatabase(): List<Station>
        fun loadStationsFromDatabaseLive(): LiveData<List<Station>>
        suspend fun updateStations(list: ArrayList<Station>)
        suspend fun updateStation(station: Station)
        fun loadCurrentStation(): LiveData<Station>
        suspend fun loadCurrentStationL(): Station?
        suspend fun updateCurrentStation(station: Station)
        fun loadShuttle(): LiveData<Shuttle>
    }

    interface Interactor {
        suspend fun deleteStations()
        suspend fun loadStationsFromServer(): ArrayList<Station>
        suspend fun loadStations(): List<Station>
        fun loadStationsLive(): LiveData<List<Station>>
        suspend fun updateStation(station: Station)
        fun loadCurrentStation(): LiveData<Station>
        suspend fun loadCurrentStationL(): Station?
        suspend fun updateCurrentStation(station: Station)
        fun loadShuttle(): LiveData<Shuttle>
    }
}