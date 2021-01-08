package com.asmatullah.spaceapp.common.core.di

import com.asmatullah.spaceapp.common.core.BuildConfig
import com.asmatullah.spaceapp.common.core.db.local.LocalDatabase
import com.asmatullah.spaceapp.common.core.db.local.dao.ShuttleDAO
import com.asmatullah.spaceapp.common.core.db.local.dao.StationDAO
import com.asmatullah.spaceapp.common.core.db.local.preference.Session
import com.asmatullah.spaceapp.common.core.db.network.NetworkBuilder
import com.asmatullah.spaceapp.common.core.db.network.NetworkService
import com.asmatullah.spaceapp.common.core.db.network.NetworkServiceImpl
import com.asmatullah.spaceapp.common.core.db.network.environment.Environment
import com.asmatullah.spaceapp.common.core.db.network.environment.EnvironmentManager
import com.asmatullah.spaceapp.common.core.util.ThemeController
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

object CommonModule {

    fun createModule(
        okHttpBuilder: (OkHttpClient.Builder.() -> OkHttpClient.Builder)? = null,
        retrofitBuilder: (Retrofit.Builder.() -> Retrofit.Builder)? = null,
        gsonBuilder: (GsonBuilder.() -> GsonBuilder)? = null,
        restartKoin: () -> Unit
    ): Module = module {

        factory(named("restartKoin")) { restartKoin }

        single {
            EnvironmentManager.loadEnvironment(get())
        } bind Environment::class

        single {
            GsonBuilder()
                .apply {
                    if (BuildConfig.DEBUG) setPrettyPrinting()
                    gsonBuilder?.invoke(this)
                }
                .create()
        } bind Gson::class

        single { NetworkBuilder.initRetrofit(get<Environment>().baseAddress) }

        single {
            Session(get())
        } bind Session::class

        single {
            ThemeController(get())
        } bind ThemeController::class

        single {
            LocalDatabase.initDatabase(get())
        }

        single {
            get<LocalDatabase>().stationDAO()
        } bind StationDAO::class

        single {
            get<LocalDatabase>().shuttleDAO()
        } bind ShuttleDAO::class

        single {
            NetworkServiceImpl(get())
        } bind NetworkService::class
    }
}