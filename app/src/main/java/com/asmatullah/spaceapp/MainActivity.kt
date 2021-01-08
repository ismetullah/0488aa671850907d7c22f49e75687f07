package com.asmatullah.spaceapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.asmatullah.spaceapp.common.core.db.local.preference.Session
import com.asmatullah.spaceapp.common.core.ui.navigation.popAndReplaceFeature
import com.asmatullah.spaceapp.common.core.ui.navigation.replaceFeature
import com.asmatullah.spaceapp.home.ui.HomeFragment
import com.asmatullah.spaceapp.login.LoginContract
import com.asmatullah.spaceapp.login.LoginFragment
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), LoginContract.Navigator {
    private val session by inject<Session>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (session.hasSeenLogin) {
            replaceFeature(fragment = HomeFragment())
        } else {
            replaceFeature(fragment = LoginFragment())
        }
    }

    override fun openHomeScreen() {
        popAndReplaceFeature(replaceTarget = HomeFragment())
    }
}