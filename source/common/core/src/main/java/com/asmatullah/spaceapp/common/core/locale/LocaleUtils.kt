package com.asmatullah.spaceapp.common.core.locale

import java.util.*

object LocaleUtils {
    private var locale: Locale = Locale.getDefault()
    private var lastLanguage: String = locale.language

    fun getCurrentLocale(): Locale {
        return locale
    }

    private fun updateLocale(currentLanguage: String): Boolean {
        val isLocaleChanged = currentLanguage.equals(lastLanguage, ignoreCase = true)

        if (isLocaleChanged) {
            lastLanguage = currentLanguage
            locale = Locale(currentLanguage)
        }

        return isLocaleChanged
    }

    fun getLanguageList(): List<Language> {
        val en = Language("English", "en")
        val rus = Language("Russian", "ru")
        return listOf(en, rus)
    }
}