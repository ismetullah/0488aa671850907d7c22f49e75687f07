package com.asmatullah.spaceapp.common.core.util

import android.content.Context
import android.provider.Settings
import kotlinx.coroutines.withTimeout
import java.io.File

private const val PROCESS_EXECUTE_TIMEOUT_MILLIS = 1000L

object AndroidUtils {

    suspend fun isRooted(): Boolean {
        // get from build info
        val buildTags = android.os.Build.TAGS
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true
        }

        // check if /system/app/Superuser.apk is present
        try {
            val file = File("/system/app/Superuser.apk")
            if (file.exists()) {
                return true
            }
        } catch (ignored: Exception) {
            // ignored
        }

        return withTimeout(PROCESS_EXECUTE_TIMEOUT_MILLIS) {
            canExecuteCommand("/system/xbin/which su") ||
                    canExecuteCommand("/system/bin/which su") ||
                    canExecuteCommand("which su")
        }
    }

    private fun canExecuteCommand(command: String): Boolean {
        var process: Process? = null
        var result: Boolean

        try {
            process = Runtime.getRuntime().exec(command)
            val code = process?.waitFor()
            result = code == 0
        } catch (e: Exception) {
            result = false
        } finally {
            process?.destroy()
        }
        return result
    }

    fun getUniqueDeviceId(context: Context): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }
}