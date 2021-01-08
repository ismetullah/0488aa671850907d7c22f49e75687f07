package com.asmatullah.spaceapp.login

import com.asmatullah.spaceapp.common.core.db.local.preference.Session
import com.asmatullah.spaceapp.common.core.ui.mvvm.BaseViewModel
import com.asmatullah.spaceapp.common.uikit.widget.CustomSeekbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val interactor: LoginContract.Interactor,
    private val session: Session,
    private var navigator: LoginContract.Navigator
) : BaseViewModel(), LoginContract.ViewModel {

    override fun onClickEnter(
        etName: TextInputEditText,
        seekBarStrength: CustomSeekbar,
        seekBarSpeed: CustomSeekbar,
        seekBarCapacity: CustomSeekbar
    ) {
        if (etName.text.isNullOrBlank()) {
            errorResource.postValue(R.string.error_empty_name)
            return
        }
        handleRequest {
            interactor.insertShuttle(
                etName.text!!.toString().trim(),
                seekBarStrength.getProgress(),
                seekBarCapacity.getProgress(),
                seekBarSpeed.getProgress()
            )
            session.hasSeenLogin = true
            withContext(Dispatchers.Main) {
                navigator.openHomeScreen()
            }
        }
    }
}