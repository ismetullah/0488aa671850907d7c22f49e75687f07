package com.asmatullah.spaceapp.home.ui.stations.di

import com.asmatullah.spaceapp.common.core.di.InjectionModule
import com.asmatullah.spaceapp.home.ui.stations.StationsContract
import com.asmatullah.spaceapp.home.ui.stations.StationsViewModel
import com.asmatullah.spaceapp.home.ui.stations.data.StationsInteractor
import com.asmatullah.spaceapp.home.ui.stations.data.StationsRepo
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

object StationsModule : InjectionModule {
    override fun create() = module {
        factory { StationsRepo(get(), get(), get()) } bind StationsContract.Repo::class
        factory { StationsInteractor(get()) } bind StationsContract.Interactor::class
        viewModel { StationsViewModel(get()) }
    }
}