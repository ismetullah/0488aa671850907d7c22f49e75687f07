package com.asmatullah.spaceapp

import android.app.Application
import com.asmatullah.spaceapp.common.core.di.CommonModule
import com.asmatullah.spaceapp.common.core.util.ThemeController
import com.asmatullah.spaceapp.home.HomeFeatureModule
import com.asmatullah.spaceapp.login.di.LoginFeatureModule
import io.palaima.debugdrawer.timber.data.LumberYard
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.KoinContextHandler
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initTimber()
        initKoin()
        KoinContextHandler.get().get<ThemeController>().applyMode()
    }

    fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG) androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                createCommonModule(),
                LoginFeatureModule.create(),
                HomeFeatureModule.create()
            )
        }
    }

    private fun initTimber() {
        val lumberYard = LumberYard.getInstance(this)
        lumberYard.cleanUp()
        Timber.plant(lumberYard.tree())
        Timber.plant(Timber.DebugTree())
    }

    private fun createCommonModule(): Module = CommonModule.createModule(
        restartKoin = {
            KoinContextHandler.stop()
            initKoin()
        }
    )
}