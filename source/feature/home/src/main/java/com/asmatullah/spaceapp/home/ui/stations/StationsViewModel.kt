package com.asmatullah.spaceapp.home.ui.stations

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.asmatullah.spaceapp.common.core.db.models.Shuttle
import com.asmatullah.spaceapp.common.core.db.models.Station
import com.asmatullah.spaceapp.common.core.ui.mvvm.BaseViewModel
import com.asmatullah.spaceapp.common.core.util.Constants.DS_DAMAGE_VALUE
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
    override val damageLeft = MutableLiveData<Int>()

    override val gameOver = MutableLiveData(false)
    override var shouldScroll = false

    private var stationEarth: Station? = null
    private val defaultStation = Station("", 0.0, 0.0, 0, 0, 0)
    private lateinit var countDownTimer: CountDownTimer

    override fun loadStations() {
        handleRequest {
            interactor.deleteStations()
            interactor.loadStations()
        }
    }

    fun updateVariables(shuttle: Shuttle) {
        UGS.postValue(calculateInitialUGS(shuttle))
        EUS.postValue(calculateInitialEUS(shuttle))
        val ds = calculateInitialDS(shuttle)
        DS.postValue(ds)
        initDamageCountDown(ds)
        damageLeft.postValue(shuttle.damageCapacity)
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
                if (countDownTimer != null)
                    countDownTimer.cancel()
            }
        }
    }

    private suspend fun isGameOver(): Boolean {
        var gameOver = false
        withContext(Dispatchers.Default) {
            shuttle.value?.let { curShuttle ->
                if (damageLeft.value!! <= 0) {
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

    private fun initDamageCountDown(ds: Int) {
        countDownTimer = object : CountDownTimer(ds.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeleft.postValue((millisUntilFinished / 1000).toInt())
            }

            override fun onFinish() {
                timeleft.postValue(0)
                val newDamage = damageLeft.value!! - DS_DAMAGE_VALUE
                damageLeft.postValue(newDamage)
                checkForGameOver()
                this.start()
            }
        }.start()
    }
}