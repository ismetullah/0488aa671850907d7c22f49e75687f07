package com.asmatullah.spaceapp.common.uikit.error

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.postDelayed
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.asmatullah.spaceapp.common.uikit.R
import com.asmatullah.spaceapp.common.uikit.widget.TextContainer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.widget_error_bottom_sheet.view.*

private const val DELAY_IN_MILLIS = 3000L

class ErrorBottomSheet : BottomSheetDialogFragment() {

    companion object {
        fun show(
            fragment: Fragment,
            text: TextContainer,
            title: TextContainer? = null,
            shouldAutoFinish: Boolean = false
        ) {
            show(
                fragment.childFragmentManager,
                text,
                title,
                shouldAutoFinish
            )
        }

        fun show(
            activity: FragmentActivity,
            text: TextContainer,
            title: TextContainer? = null,
            shouldAutoFinish: Boolean = false
        ) {
            show(
                activity.supportFragmentManager,
                text,
                title,
                shouldAutoFinish
            )
        }

        fun show(
            fragmentManager: FragmentManager,
            text: TextContainer,
            title: TextContainer? = null,
            shouldAutoFinish: Boolean = false
        ) {
            ErrorBottomSheet()
                .apply {
                    this.title = title
                    this.text = text
                    this.shouldAutoFinish = shouldAutoFinish
                }
                .show(fragmentManager, ErrorBottomSheet::class.java.name)
        }
    }

    private var title: TextContainer? = null
    private var text: TextContainer? = null
    private var shouldAutoFinish: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // dirty hack to prevent dialog restoring
        if (savedInstanceState != null) dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext())
        val view =
            LayoutInflater.from(requireContext()).inflate(R.layout.widget_error_bottom_sheet, null)
        dialog.setContentView(view)
        (view.parent as ViewGroup).setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )

        view.titleText.isVisible = title?.applyTo(view.titleText) != null
        text?.applyTo(view.errorText)
        if (shouldAutoFinish) {
            Handler().postDelayed(DELAY_IN_MILLIS) { dismissAllowingStateLoss() }
        }

        return dialog
    }

    override fun onStop() {
        super.onStop()
        dismissAllowingStateLoss()
    }
}