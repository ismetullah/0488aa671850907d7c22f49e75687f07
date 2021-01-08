package com.asmatullah.spaceapp.common.core.di

import android.content.Context
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
import com.google.gson.JsonParser
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object CommonModule {

    private const val DEFAULT_CONNECT_TIMEOUT_SECONDS = 30L
    private const val DEFAULT_READ_TIMEOUT_SECONDS = 30L
    private const val DEFAULT_DISK_CACHE_SIZE = 256 * 1024 * 1024L

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

//        single {
//            createOkHttpClient(get())
//                .apply { okHttpBuilder?.invoke(this) }
//                .apply { if (BuildConfig.DEBUG) addLoggingInterceptor(get<Gson>()) }
//                .build()
//        } bind OkHttpClient::class

        single {
            GsonBuilder()
                .apply {
                    if (BuildConfig.DEBUG) setPrettyPrinting()
                    gsonBuilder?.invoke(this)
                }
                .create()
        } bind Gson::class

//        single {
//            Retrofit.Builder()
//                .baseUrl("${get<Environment>().restAddress}/")
//                .addConverterFactory(GsonConverterFactory.create(get<Gson>()))
//                .apply { retrofitBuilder?.invoke(this) }
//                .callFactory(
//                    // Makes OkHttp initialization lazy. Decreases startup time by ~100ms.
//                    object : Call.Factory {
//                        val okHttpClient: OkHttpClient by inject()
//                        override fun newCall(request: Request): Call = okHttpClient.newCall(request)
//                    }
//                )
//                .build()
//        } bind Retrofit::class

        single { NetworkBuilder.initRetrofit(get<Environment>().baseAddress) }
//
//        single {
//            get<Retrofit>().create(ForumService::class.java)
//        }

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

    private fun Scope.createOkHttpClient(
        context: Context
    ): OkHttpClient.Builder {
        val trustManager = get<TrustManagerFactory>().trustManagers[0] as X509TrustManager
        val socketFactory = get<SSLContext>().socketFactory

        return OkHttpClient.Builder()
            .readTimeout(DEFAULT_CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .cache(
                Cache(
                    context.cacheDir,
                    DEFAULT_DISK_CACHE_SIZE
                )
            )
            .sslSocketFactory(socketFactory, trustManager)
            .hostnameVerifier(HostnameVerifier { _, _ -> true })
    }

    private fun OkHttpClient.Builder.addLoggingInterceptor(gson: Gson): OkHttpClient.Builder {
        val okHttpLogTag = "OkHttp"

        val okHttpLogger = object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                if (!message.startsWith('{') && !message.startsWith('[')) {
                    Timber.tag(okHttpLogTag).d(message)
                    return
                }

                try {
                    val json = JsonParser.parseString(message)
                    Timber.tag(okHttpLogTag).d(gson.toJson(json))
                } catch (e: Throwable) {
                    Timber.tag(okHttpLogTag).d(message)
                }
            }
        }

        val interceptor = HttpLoggingInterceptor(okHttpLogger).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return addInterceptor(interceptor)
    }
}