package com.asmatullah.spaceapp.common.core.ui.navigation

import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.fragment.app.commit
import com.asmatullah.spaceapp.common.core.R
import com.asmatullah.spaceapp.common.core.ui.BaseFragment
import com.asmatullah.spaceapp.common.core.ui.util.appendArgs
import com.asmatullah.spaceapp.common.core.ui.util.hideKeyboard
import kotlin.reflect.KClass

private const val PREVIOUS_FRAGMENT_TAG_ARG = "PREVIOUS_FRAGMENT_TAG_ARG"

private fun Fragment.getPreviousTag(): String? = arguments?.getString(PREVIOUS_FRAGMENT_TAG_ARG)
fun Fragment.getCurrentScreen(): Fragment? =
    childFragmentManager.findFragmentById(R.id.featureContent)

fun FragmentActivity.getCurrentFeature(): Fragment? =
    supportFragmentManager.findFragmentById(R.id.container)

fun Fragment.popScreen() {
    requireActivity().hideKeyboard()

    val previousTag = getCurrentScreen()?.getPreviousTag()

    try {
        when {
            previousTag != null -> popScreenTo(previousTag, true)
            childFragmentManager.backStackEntryCount > 0 -> childFragmentManager.popBackStackImmediate()
            else -> requireActivity().supportFragmentManager.popBackStackImmediate()
        }
    } catch (e: IllegalStateException) {
        // Ignore
    }
}

fun FragmentActivity.popFeature() {
    hideKeyboard()
    try {
        when {
            supportFragmentManager.backStackEntryCount < 2 -> finish()
            else -> supportFragmentManager.popBackStackImmediate()
        }
    } catch (e: IllegalStateException) {
        // Ignore
    }
}

fun <T : Fragment> Fragment.popScreenTo(target: KClass<T>, inclusive: Boolean = false) {
    popScreenTo(target.java.name, inclusive)
}

private fun Fragment.popScreenTo(tag: String, inclusive: Boolean = false) {
    val flag = if (inclusive) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0

    requireActivity().hideKeyboard()

    with(childFragmentManager) {
        if (backStackEntryCount == 0 || getBackStackEntryAt(0).name == tag && inclusive) {
            requireActivity().popFeature()
        } else {
            try {
                if (!popBackStackImmediate(tag, flag)) requireActivity().popFeature()
            } catch (e: IllegalStateException) {
                // Ignore
            }
        }
    }
}

fun <T : Fragment> FragmentActivity.popFeatureTo(target: KClass<T>, inclusive: Boolean = false) {
    val flag = if (inclusive) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0

    hideKeyboard()
    with(supportFragmentManager) {
        if (backStackEntryCount == 0 || getBackStackEntryAt(0).name == target::java.name && inclusive) {
            finish()
        } else {
            try {
                if (!popBackStackImmediate(target::java.name, flag)) finish()
            } catch (e: IllegalStateException) {
                // Ignore
            }
        }
    }
}

fun Fragment.replaceScreen(
    fragment: Fragment,
    popCurrent: Boolean = false,
    tag: String = fragment::class.java.name,
    @AnimRes enter: Int = R.anim.nav_enter,
    @AnimRes exit: Int = R.anim.nav_exit,
    @AnimRes popEnter: Int = R.anim.nav_pop_enter,
    @AnimRes popExit: Int = R.anim.nav_pop_exit,
    fragmentManager: FragmentManager = childFragmentManager
) {
    // Since it may be used by fragments that are nested and contained in child FM
    requireActivity().hideKeyboard()
    fragmentManager.commit(allowStateLoss = true) {
        setCustomAnimations(enter, exit, popEnter, popExit)
        if (popCurrent) {
            getCurrentScreen()
                ?.let { it.getPreviousTag() ?: it::class.java.name }
                ?.let { fragment.appendArgs(PREVIOUS_FRAGMENT_TAG_ARG to it) }
        }
        replace(R.id.featureContent, fragment, tag)
        addToBackStack(tag)
    }
}

fun Fragment.addScreen(
    fragment: Fragment,
    @IdRes layoutId: Int = R.id.featureContent,
    requestCode: Int? = null,
    tag: String = fragment::class.java.name,
    @AnimRes enter: Int = R.anim.nav_enter,
    @AnimRes exit: Int = R.anim.nav_exit,
    @AnimRes popEnter: Int = R.anim.nav_pop_enter,
    @AnimRes popExit: Int = R.anim.nav_pop_exit,
    fragmentManager: FragmentManager = childFragmentManager
) {
    // Since it may be used by fragments that are nested and contained in child FM
    requireActivity().hideKeyboard()
    fragmentManager.commit(allowStateLoss = true) {
        setCustomAnimations(enter, exit, popEnter, popExit)
        add(layoutId, fragment)
        addToBackStack(tag)
        if (requestCode != null) fragment.setTargetFragment(this@addScreen, requestCode)
    }
}

fun FragmentActivity.replaceFeature(
    fragment: Fragment,
    @IdRes layoutId: Int = R.id.container,
    popCurrent: Boolean = false,
    tag: String = fragment::class.java.name,
    @AnimRes enter: Int = R.anim.nav_enter,
    @AnimRes exit: Int = R.anim.nav_exit,
    @AnimRes popEnter: Int = R.anim.nav_pop_enter,
    @AnimRes popExit: Int = R.anim.nav_pop_exit,
    fragmentManager: FragmentManager = supportFragmentManager
) {
    hideKeyboard()
    if (popCurrent) fragmentManager.popBackStack()
    fragmentManager.commit(allowStateLoss = true) {
        setCustomAnimations(enter, exit, popEnter, popExit)
        replace(layoutId, fragment)
        addToBackStack(tag)
    }
    supportFragmentManager.executePendingTransactions()
}

fun FragmentActivity.addFeature(
    fragment: Fragment,
    @IdRes layoutId: Int = R.id.container,
    popCurrent: Boolean = false,
    tag: String = fragment::class.java.name,
    @AnimRes enter: Int = R.anim.nav_enter,
    @AnimRes exit: Int = R.anim.nav_exit,
    @AnimRes popEnter: Int = R.anim.nav_pop_enter,
    @AnimRes popExit: Int = R.anim.nav_pop_exit,
    fragmentManager: FragmentManager = supportFragmentManager
) {
    hideKeyboard()
    if (popCurrent) fragmentManager.popBackStack()
    fragmentManager.commit(allowStateLoss = true) {
        setCustomAnimations(enter, exit, popEnter, popExit)
        add(layoutId, fragment)
        addToBackStack(tag)
    }
    supportFragmentManager.executePendingTransactions()
}

fun Fragment.popAndReplace(
    replaceTarget: Fragment,
    popTarget: KClass<out Fragment>? = null,
    popTo: KClass<out Fragment>? = null,
    inclusive: Boolean = false,
    @IdRes layoutId: Int = R.id.featureContent,
    @AnimRes enter: Int = R.anim.nav_enter,
    @AnimRes exit: Int = R.anim.nav_exit,
    @AnimRes popEnter: Int = R.anim.nav_pop_enter,
    @AnimRes popExit: Int = R.anim.nav_pop_exit,
    fragmentManager: FragmentManager = parentFragment?.childFragmentManager ?: childFragmentManager
) {
    requireActivity().hideKeyboard()

    with(fragmentManager) {
        // Override exit animation for popping fragment
        val poppingFragment =
            if (parentFragment == null) getCurrentScreen() else this@popAndReplace
        if (poppingFragment is BaseFragment<*>) {
            poppingFragment.overrideExitAnimation(exit)
        }

        // Make pop entering fragment invisible during transition
        popTo?.java?.name
            ?.let { findFragmentByTag(it) as? BaseFragment<*> }
            ?.apply { overrideEnterAnimation(R.anim.nav_stay_transparent) }

        try {
            // Perform pop back stack
            popBackStackImmediate(
                popTo?.java?.name,
                if (inclusive && popTo != null) POP_BACK_STACK_INCLUSIVE else 0
            )
        } catch (e: IllegalStateException) {
            // Ignore error
        }
        commit(allowStateLoss = true) {
            // Preform immediate replace
            setCustomAnimations(enter, 0, popEnter, popExit)
            replace(layoutId, replaceTarget, replaceTarget.javaClass.name)
            addToBackStack(replaceTarget.javaClass.name)
        }

    }
}

fun FragmentActivity.popAndReplaceFeature(
    replaceTarget: Fragment,
    popTarget: KClass<out Fragment>? = null,
    popTo: KClass<out Fragment>? = null,
    inclusive: Boolean = false,
    @IdRes layoutId: Int = R.id.container,
    @AnimRes enter: Int = R.anim.nav_enter,
    @AnimRes exit: Int = R.anim.nav_exit,
    @AnimRes popEnter: Int = R.anim.nav_pop_enter,
    @AnimRes popExit: Int = R.anim.nav_pop_exit,
    fragmentManager: FragmentManager = supportFragmentManager
) {
    hideKeyboard()

    with(fragmentManager) {
        // Override exit animation for popping fragment
        val poppingFragment =
            if (getCurrentFeature() == null) getCurrentFeature() else this
        if (poppingFragment is BaseFragment<*>) {
            poppingFragment.overrideExitAnimation(exit)
        }

        // Make pop entering fragment invisible during transition
        popTo?.java?.name
            ?.let { findFragmentByTag(it) as? BaseFragment<*> }
            ?.apply {
                overrideEnterAnimation(R.anim.nav_stay_transparent)
            }

        try {
            // Perform pop back stack
            popBackStackImmediate(
                popTo?.java?.name,
                if (inclusive && popTo != null) POP_BACK_STACK_INCLUSIVE else 0
            )
        } catch (e: IllegalStateException) {
            // Ignore error
        }
        commit(allowStateLoss = true) {
            // Preform immediate replace
            setCustomAnimations(enter, 0, popEnter, popExit)
            replace(layoutId, replaceTarget, replaceTarget.javaClass.name)
            addToBackStack(replaceTarget.javaClass.name)
        }

    }
}