package com.asmatullah.spaceapp.common.uikit.common

import android.view.View
import android.widget.AdapterView

abstract class SpinnerItenSelectedListener : AdapterView.OnItemSelectedListener {

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onItemSelected(parent, position)
    }

    abstract fun onItemSelected(parent: AdapterView<*>?, position: Int)
}