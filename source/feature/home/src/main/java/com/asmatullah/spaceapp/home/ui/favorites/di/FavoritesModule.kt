package com.asmatullah.spaceapp.home.ui.favorites.di

import com.asmatullah.spaceapp.common.core.di.InjectionModule
import com.asmatullah.spaceapp.home.ui.favorites.FavoritesContract
import com.asmatullah.spaceapp.home.ui.favorites.FavoritesViewModel
import com.asmatullah.spaceapp.home.ui.favorites.data.FavoritesInteractor
import com.asmatullah.spaceapp.home.ui.favorites.data.FavoritesRepo
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

object FavoritesModule : InjectionModule {
    override fun create() = module {
        factory { FavoritesRepo(get()) } bind FavoritesContract.Repo::class
        factory { FavoritesInteractor(get()) } bind FavoritesContract.Interactor::class
        viewModel { FavoritesViewModel(get()) }
    }
}