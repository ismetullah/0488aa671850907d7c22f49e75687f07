package com.asmatullah.spaceapp.common.core.ui.mvvm

import androidx.annotation.StringRes

interface BaseView {
    fun onError(text: String)
    fun onError(@StringRes text: Int)
}