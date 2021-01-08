package com.asmatullah.spaceapp.common.core.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<DB : ViewDataBinding>(private val layoutId: Int) : AppCompatActivity() {

    protected lateinit var binding: DB

    abstract fun configureUI()

    protected fun readIntentData(intent: Intent?) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        readIntentData(intent)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        configureUI()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        readIntentData(intent)
    }
}