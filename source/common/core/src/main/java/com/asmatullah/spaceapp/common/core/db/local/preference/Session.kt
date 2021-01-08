package com.asmatullah.spaceapp.common.core.db.local.preference

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.asmatullah.spaceapp.common.core.db.network.environment.*

private const val PRIVATE_PREF_NAME = "SPACEAPP_PREF_NAME"

class Session(context: Context) : PreferenceSettings(
    context,
    PRIVATE_PREF_NAME
) {
    companion object {
        private const val KEY_HAS_SEEN_LOGIN = "KEY_HAS_SEEN_LOGIN"

        private const val KEY_NIGHT_MODE = "KEY_NIGHT_MODE"

        private const val KEY_ENVIRONMENT_ADDRESS = "ENVIRONMENT_ADDRESS"
        private const val KEY_ENVIRONMENT_PORT = "ENVIRONMENT_PORT"
        private const val KEY_ENVIRONMENT_API_VERSION = "ENVIRONMENT_API_VERSION"
        private const val KEY_ENVIRONMENT_SSL_ENABLED = "ENVIRONMENT_SSL_ENABLED"
        private const val KEY_ENVIRONMENT_PATH = "ENVIRONMENT_PATH"
    }

    var hasSeenLogin: Boolean
        get() = getBoolean(KEY_HAS_SEEN_LOGIN)
        set(value) = putBoolean(KEY_HAS_SEEN_LOGIN, value).apply()

    var nightMode: Int
        get() = getInt(KEY_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        set(value) = putInt(KEY_NIGHT_MODE, value).apply()

    var environmentAddress: String
        get() = getString(KEY_ENVIRONMENT_ADDRESS, DEFAULT_ADDRESS)
        set(value) = putString(KEY_ENVIRONMENT_ADDRESS, value).apply()

    var environmentPort: Int
        get() = getInt(KEY_ENVIRONMENT_PORT, DEFAULT_PORT)
        set(value) = putInt(KEY_ENVIRONMENT_PORT, value).apply()

    var environmentSSLEnabled: Boolean
        get() = getBoolean(KEY_ENVIRONMENT_SSL_ENABLED, DEFAULT_IS_SSL_ENABLED)
        set(value) = putBoolean(KEY_ENVIRONMENT_SSL_ENABLED, value).apply()

    var environmentAPIVersion: Int
        get() = getInt(KEY_ENVIRONMENT_API_VERSION, DEFAULT_VERSION)
        set(value) = putInt(KEY_ENVIRONMENT_API_VERSION, value).apply()

    var environmentPath: String
        get() = getString(KEY_ENVIRONMENT_PATH, DEFAULT_PATH)
        set(value) = putString(KEY_ENVIRONMENT_PATH, value).apply()
}