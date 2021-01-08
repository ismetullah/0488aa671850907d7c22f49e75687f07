package com.asmatullah.spaceapp.login

import com.asmatullah.spaceapp.common.uikit.widget.CustomSeekbar
import com.google.android.material.textfield.TextInputEditText

interface LoginContract {
    interface ViewModel {
        fun onClickEnter(
            etName: TextInputEditText,
            seekBarStrength: CustomSeekbar,
            seekBarSpeed: CustomSeekbar,
            seekBarCapacity: CustomSeekbar
        )
    }

    interface Repo {
        suspend fun insertShuttle(name: String, strength: Int, capacity: Int, speed: Int)
    }

    interface Interactor {
        suspend fun insertShuttle(name: String, strength: Int, capacity: Int, speed: Int)
    }

    interface Navigator {
        fun openHomeScreen()
    }
}