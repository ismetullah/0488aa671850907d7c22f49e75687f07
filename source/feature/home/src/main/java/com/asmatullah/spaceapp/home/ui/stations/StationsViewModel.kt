package com.asmatullah.spaceapp.home.ui.stations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.asmatullah.spaceapp.common.core.db.models.Shuttle
import com.asmatullah.spaceapp.common.core.db.models.Station
import com.asmatullah.spaceapp.common.core.ui.mvvm.BaseViewModel

class StationsViewModel(private val interactor: StationsContract.Interactor) : BaseViewModel(),
    StationsContract.ViewModel {

    override var stations = MutableLiveData<List<Station>>(arrayListOf())
    override var shuttle: LiveData<Shuttle> = interactor.loadShuttle()
    override val UGS = MutableLiveData<Int>()
    override val EUS = MutableLiveData<Int>()
    override val DS = MutableLiveData<Int>()
    override val timeleft = MutableLiveData<Int>()

    fun initialize() {
        UGS.postValue(5000)
        EUS.postValue(500)
        DS.postValue(50000)

        handleRequest {
            stations.postValue(interactor.loadStationsFromServer())
        }
    }
}