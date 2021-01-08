package com.asmatullah.spaceapp.common.core.db.network

import com.asmatullah.spaceapp.common.core.BuildConfig
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

object NetworkBuilder {
    private const val TIMEOUT_INTERVAL = 1L

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val okHttpLogger = object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                if (!message.startsWith('{') && !message.startsWith('[')) {
                    Timber.tag("Okhttp").d(message)
                    return
                }

                try {
                    Timber.tag("Okhttp").d(Gson().toJson(message))
                } catch (e: Throwable) {
                    Timber.tag("Okhttp").d(message)
                }
            }
        }
        val logger = HttpLoggingInterceptor(okHttpLogger)
        logger.level = HttpLoggingInterceptor.Level.BODY
        return logger
    }

    private fun initOkHttpClient(): OkHttpClient {
        val okhttp = OkHttpClient.Builder()
            .writeTimeout(TIMEOUT_INTERVAL, TimeUnit.MINUTES)
            .readTimeout(TIMEOUT_INTERVAL, TimeUnit.MINUTES)
            .connectTimeout(TIMEOUT_INTERVAL, TimeUnit.MINUTES)

        if (BuildConfig.DEBUG) {
            okhttp.addInterceptor(provideLoggingInterceptor())
        }
        return okhttp.build()
    }

    fun initRetrofit(baseUrl: String): ForumService {
        return Retrofit.Builder().baseUrl(baseUrl).client(initOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ForumService::class.java)
    }
}