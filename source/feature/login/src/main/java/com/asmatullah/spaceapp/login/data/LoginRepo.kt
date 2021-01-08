package com.asmatullah.spaceapp.login.data

import com.asmatullah.spaceapp.common.core.db.local.dao.ShuttleDAO
import com.asmatullah.spaceapp.common.core.db.models.Shuttle
import com.asmatullah.spaceapp.login.LoginContract

class LoginRepo(private val shuttleDAO: ShuttleDAO) : LoginContract.Repo {
    override suspend fun insertShuttle(name: String, strength: Int, capacity: Int, speed: Int) {
        shuttleDAO.insert(
            Shuttle(
                name = name,
                strength = strength,
                capacity = capacity,
                speed = speed
            )
        )
    }
}