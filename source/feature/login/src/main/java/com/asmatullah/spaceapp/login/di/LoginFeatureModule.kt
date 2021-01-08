package com.asmatullah.spaceapp.login.di

import com.asmatullah.spaceapp.common.core.di.InjectionModule
import com.asmatullah.spaceapp.login.LoginContract
import com.asmatullah.spaceapp.login.LoginViewModel
import com.asmatullah.spaceapp.login.data.LoginInteractor
import com.asmatullah.spaceapp.login.data.LoginRepo
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

object LoginFeatureModule : InjectionModule {
    override fun create() = module {
        factory { LoginRepo(get()) } bind LoginContract.Repo::class
        factory { LoginInteractor(get()) } bind LoginContract.Interactor::class
        viewModel { (navigator: LoginContract.Navigator) ->
            LoginViewModel(
                get(),
                get(),
                navigator
            )
        }
    }
}