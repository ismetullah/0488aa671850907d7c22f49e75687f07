package com.asmatullah.spaceapp.login.data

import com.asmatullah.spaceapp.login.LoginContract

class LoginInteractor(private val repo: LoginContract.Repo) : LoginContract.Interactor {
    override suspend fun insertShuttle(name: String, strength: Int, capacity: Int, speed: Int) {
        repo.insertShuttle(name, strength, capacity, speed)
    }
}