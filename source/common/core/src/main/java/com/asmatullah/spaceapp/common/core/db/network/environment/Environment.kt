package com.asmatullah.spaceapp.common.core.db.network.environment

class Environment(
    val baseAddress: String,
    val port: Int = -1,
    val isSslEnabled: Boolean,
    val apiVersion: Int,
    val path: String = ""
) {
    val restAddress: String = buildString {
        append(if (isSslEnabled) "https" else "http")
        append("://$baseAddress")
        if (port != -1) {
            append(":$port")
        }
        if (path.isNotBlank()) {
            append("/$path")
        }
        //append("/api/v$apiVersion")
    }

    fun buildUrl(relativePath: String) =
        if (relativePath.isBlank()) {
            relativePath
        } else {
            "$restAddress/${relativePath.trimStart('/')}"
        }
}