package com.asmatullah.spaceapp.common.core.db.network.environment

import com.asmatullah.spaceapp.common.core.BuildConfig
import com.asmatullah.spaceapp.common.core.db.local.preference.Session

const val DEFAULT_ADDRESS = BuildConfig.BASE_URL
const val DEFAULT_PATH = ""
const val DEFAULT_PORT = -1
const val DEFAULT_VERSION = 1
const val DEFAULT_IS_SSL_ENABLED = false

object EnvironmentManager {

    fun loadEnvironment(session: Session) =
        Environment(
            session.environmentAddress,
            session.environmentPort,
            session.environmentSSLEnabled,
            session.environmentAPIVersion,
            session.environmentPath
        )

    fun saveEnvironment(
        session: Session,
        environment: Environment
    ) {
        with(environment) {
            session.environmentAddress = baseAddress
            session.environmentPort = port
            session.environmentSSLEnabled = isSslEnabled
            session.environmentAPIVersion = apiVersion
            session.environmentPath = path
        }
    }
}