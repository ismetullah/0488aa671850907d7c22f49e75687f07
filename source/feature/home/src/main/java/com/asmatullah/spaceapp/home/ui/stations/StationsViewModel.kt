package com.asmatullah.spaceapp.home.ui.stations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.asmatullah.spaceapp.common.core.db.models.Shuttle
import com.asmatullah.spaceapp.common.core.db.models.Station
import com.asmatullah.spaceapp.common.core.ui.mvvm.BaseViewModel
import com.asmatullah.spaceapp.home.R
import com.asmatullah.spaceapp.home.util.calculateEUS
import com.asmatullah.spaceapp.home.util.calculateInitialDS
import com.asmatullah.spaceapp.home.util.calculateInitialEUS
import com.asmatullah.spaceapp.home.util.calculateInitialUGS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StationsViewModel(private val interactor: StationsContract.Interactor) : BaseViewModel(),
    StationsContract.ViewModel {

    override var sortedStations = MutableLiveData<List<Station>>(arrayListOf())
    override var stations = interactor.loadStationsLive()
    override var currentStation = interactor.loadCurrentStation()
    override var shuttle: LiveData<Shuttle> = interactor.loadShuttle()
    override val UGS = MutableLiveData<Int>()
    override val EUS = MutableLiveData<Double>()
    override val DS = MutableLiveData<Int>()
    override val timeleft = MutableLiveData<Int>()

    val gameOver = MutableLiveData(false)
    // Indicates whether the recview should scroll to the currentStation
    var shouldScroll = false

    private var stationEarth: Station? = null
    private val defaultStation = Station("", 0.0, 0.0, 0, 0, 0)


    fun initialize() {
        handleRequest {
            interactor.deleteStations()
            interactor.loadStations()
        }
    }

    fun updateVariables(shuttle: Shuttle) {
        UGS.postValue(calculateInitialUGS(shuttle))
        EUS.postValue(calculateInitialEUS(shuttle))
        DS.postValue(calculateInitialDS(shuttle))
    }

    override fun initCurrentStation() {
        if (stations.value.isNullOrEmpty()) return
        handleRequest {
            stationEarth = stations.value!![0]
            interactor.updateCurrentStation(stations.value!![0])
            shouldScroll = true
        }
    }

    override fun onClickFav(station: Station) {
        handleRequest {
            station.isFav = !station.isFav
            interactor.updateStation(station)
        }
    }

    override fun onClickTravel(station: Station) {
        handleRequest {
            val eus = calculateEUS(station, currentStation.value ?: defaultStation)
            // Check if the user can travel there.
            if (UGS.value!! < station.need || EUS.value!! < eus) {
                errorResource.postValue(R.string.warning_cannot_travel)
                return@handleRequest
            }
            UGS.postValue(UGS.value!! - station.need)
            EUS.postValue(EUS.value!! - eus)
            station.stock += station.need
            station.need = 0
            interactor.updateCurrentStation(station)
            shouldScroll = true
            checkForGameOver()
        }
    }

    override fun sortStations() {
        if (stations.value.isNullOrEmpty()) return
        handleRequest {
            sortedStations.postValue(internalSortList(ArrayList(stations.value!!)))
        }
    }

    private suspend fun internalSortList(list: ArrayList<Station>): ArrayList<Station> {
        withContext(Dispatchers.IO) {
            list.sortWith { lhs, rhs ->
                if (rhs == null) return@sortWith 1
                if (lhs == null) return@sortWith -1

                if (lhs.name == rhs.name) return@sortWith 0

                if (lhs.name == currentStation.value?.name)
                    return@sortWith -1

                val distance1: Double = calculateEUS(lhs, currentStation.value ?: defaultStation)
                val distance2: Double = calculateEUS(rhs, currentStation.value ?: defaultStation)
                return@sortWith distance1.compareTo(distance2)
            }
        }
        return list
    }


    private fun checkForGameOver() {
        handleRequest {
            if (isGameOver()) {
                gameOver.postValue(true)
                stationEarth?.let { interactor.updateCurrentStation(it) }
                shouldScroll = true
            }
        }
    }

    private suspend fun isGameOver(): Boolean {
        var gameOver = false
        withContext(Dispatchers.Default) {
            shuttle.value?.let { curShuttle ->
                if (curShuttle.damageCapacity <= 0) {
                    // if damageCapacity is 0 or less, the game is over
                    gameOver = true;
                } else {
                    // Iterate through stations and check if a station exists where
                    // station.need <= UGS && distance to that station is less than EUS
                    // If such a station doesnt exist, the game is over.
                    var exists = false
                    val curStation = currentStation.value!!
                    stations.value?.forEach { station ->
                        if ((station.name != curStation.name) &&
                            (0 < station.need && station.need <= UGS.value!!) &&
                            calculateEUS(curStation, station) <= EUS.value!!
                        ) {
                            exists = true
                        }
                    }
                    gameOver = !exists
                }
            }
        }
        return gameOver
    }
}