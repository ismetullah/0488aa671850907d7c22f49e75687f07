package com.asmatullah.spaceapp.common.core.util

import androidx.appcompat.app.AppCompatDelegate
import com.asmatullah.spaceapp.common.core.db.local.preference.Session

/**
 * Separate this one from profile to optimize startup time.
 */
class ThemeController(
    private val session: Session
) {

    fun applyMode() {
        AppCompatDelegate.setDefaultNightMode(getMode())
    }

    fun setMode(@AppCompatDelegate.NightMode mode: Int) {
        session.nightMode = mode
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    @AppCompatDelegate.NightMode
    fun getMode() = session.nightMode
}