package com.asmatullah.spaceapp.common.core.locale

import android.content.Context
import android.os.Build
import java.util.*

object LocaleManager {

    fun updateLocale(context: Context, locale: Locale) = with(context.resources) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale)
        } else {
            configuration.locale = locale
        }
        context.createConfigurationContext(configuration)
        updateConfiguration(configuration, displayMetrics)
        configuration
    }
}