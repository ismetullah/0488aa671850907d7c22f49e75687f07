package com.asmatullah.spaceapp.home

import com.asmatullah.spaceapp.common.core.di.InjectionModule
import com.asmatullah.spaceapp.home.ui.favorites.di.FavoritesModule
import com.asmatullah.spaceapp.home.ui.stations.di.StationsModule
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object HomeFeatureModule : InjectionModule {
    override fun create() = module {
        loadKoinModules(FavoritesModule.create())
        loadKoinModules(StationsModule.create())
    }
}