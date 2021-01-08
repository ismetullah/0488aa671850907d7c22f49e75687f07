package com.asmatullah.spaceapp.home.ui.favorites

import androidx.lifecycle.LiveData
import com.asmatullah.spaceapp.common.core.db.models.Station

interface FavoritesContract {
    interface ViewModel {
        val favorites: LiveData<List<Station>>
    }

    interface Repo {
        fun getFavorites(): LiveData<List<Station>>
        suspend fun updateStation(station: Station)
    }

    interface Interactor {
        fun getFavorites(): LiveData<List<Station>>
        suspend fun updateStation(station: Station)
    }
}