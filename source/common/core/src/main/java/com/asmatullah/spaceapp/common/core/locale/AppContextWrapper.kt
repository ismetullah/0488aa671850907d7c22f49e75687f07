package com.asmatullah.spaceapp.common.core.locale

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.LocaleList
import java.util.*

fun wrap(context: Context?, language: String = "en"): ContextWrapper? {
    context?.resources?.apply {
        val newLocale = Locale(language)
        val result = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                configuration.setLocale(newLocale)
                val localeList = LocaleList(newLocale)
                LocaleList.setDefault(localeList)
                configuration.setLocales(localeList)
                context.createConfigurationContext(configuration)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 -> {
                configuration.setLocale(newLocale)
                context.createConfigurationContext(configuration)
            }
            else -> {
                configuration.locale = newLocale
                updateConfiguration(configuration, displayMetrics)
                context
            }
        }
        return AppContextWrapper(result)
    }
    return null
}

class AppContextWrapper(context: Context?) : ContextWrapper(context)