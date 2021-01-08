package com.asmatullah.spaceapp.common.core.ui.mvvm

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import com.asmatullah.spaceapp.common.core.BR
import com.asmatullah.spaceapp.common.core.ui.BaseActivity
import com.asmatullah.spaceapp.common.core.ui.WarningDialog

abstract class BaseMvvmActivity<V : BaseView, P : BaseViewModel, DB : ViewDataBinding>(layoutId: Int) :
    BaseActivity<DB>(layoutId), BaseView {

    abstract val viewModel: P

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(BR.viewModel, viewModel)
    }

    override fun onError(@StringRes text: Int) {
        onError(getString(text))
    }

    override fun onError(text: String) {
        val spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            Html.fromHtml(text);
        }
        WarningDialog(this).setMessage(spanned.trim()).show()
    }
}