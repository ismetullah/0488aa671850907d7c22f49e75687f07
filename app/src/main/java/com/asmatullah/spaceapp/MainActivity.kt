package com.asmatullah.spaceapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.asmatullah.spaceapp.common.core.ui.navigation.popAndReplaceFeature
import com.asmatullah.spaceapp.common.core.ui.navigation.replaceFeature
import com.asmatullah.spaceapp.home.ui.HomeFragment
import com.asmatullah.spaceapp.login.LoginContract
import com.asmatullah.spaceapp.login.LoginFragment

class MainActivity : AppCompatActivity(), LoginContract.Navigator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFeature(fragment = LoginFragment())
    }

    override fun openHomeScreen() {
        popAndReplaceFeature(replaceTarget = HomeFragment())
    }
}