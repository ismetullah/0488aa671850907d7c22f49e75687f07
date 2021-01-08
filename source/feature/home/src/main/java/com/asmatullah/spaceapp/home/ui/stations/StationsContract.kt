package com.asmatullah.spaceapp.home.ui.stations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.asmatullah.spaceapp.common.core.db.models.Shuttle
import com.asmatullah.spaceapp.common.core.db.models.Station

interface StationsContract {
    interface ViewModel {
        var stations: MutableLiveData<List<Station>>
        var shuttle: LiveData<Shuttle>
        val UGS: MutableLiveData<Int>
        val EUS: MutableLiveData<Int>
        val DS: MutableLiveData<Int>
        val timeleft: MutableLiveData<Int>
    }

    interface Repo {
        suspend fun loadStationsFromServer(): List<Station>
        suspend fun loadStationsFromDatabase(): LiveData<List<Station>>
        fun loadShuttle(): LiveData<Shuttle>
    }

    interface Interactor {
        suspend fun loadStationsFromServer(): List<Station>
        suspend fun loadStationsFromDatabase(): LiveData<List<Station>>
        fun loadShuttle(): LiveData<Shuttle>
    }
}