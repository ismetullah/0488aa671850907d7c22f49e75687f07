package com.asmatullah.spaceapp.common.core.ui.mvvm

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import com.asmatullah.spaceapp.common.core.BR
import com.asmatullah.spaceapp.common.core.ui.BaseFragment
import com.asmatullah.spaceapp.common.core.ui.WarningDialog
import com.asmatullah.spaceapp.common.core.ui.util.observeNonNull
import timber.log.Timber

abstract class BaseMvvmFragment<P : BaseViewModel, DB : ViewDataBinding>(layoutId: Int) :
    BaseFragment<DB>(layoutId), BaseView {

    abstract val viewModel: P

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.setVariable(BR.viewModel, viewModel)
    }

    fun <T : BaseViewModel> setupViewModel(viewModel: T) {
        viewModel.errorMessage.observeNonNull(this) {
            try {
                onError(it)
            } catch (e: Throwable) {
                Timber.e(e)
            }
        }
        viewModel.errorResource.observeNonNull(this) {
            try {
                onError(it)
            } catch (e: Throwable) {
                Timber.e(e)
            }
        }
    }

    override fun onError(text: Int) {
        onError(getString(text))
    }

    override fun onError(text: String) {
        val spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            Html.fromHtml(text);
        }
        WarningDialog(requireContext()).setMessage(spanned.trim()).show()
    }
}