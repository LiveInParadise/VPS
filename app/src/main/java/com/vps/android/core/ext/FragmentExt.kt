package com.vps.android.core.ext

import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.vps.android.core.delegates.FragmentViewBindingDelegate

fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T) =
    FragmentViewBindingDelegate(this, viewBindingFactory)

fun Fragment.hideKeyboard() {
    view?.hideKeyboard()
}

private const val COUNT_DOWN_INTERVAL = 1000L

fun Fragment.initDownTimer(
    millisInFuture: Long,
    tickHandler: (Long) -> Unit,
    finishHandler: () -> Unit,
): CountDownTimer {
    val cdt = object : CountDownTimer(millisInFuture, COUNT_DOWN_INTERVAL) {

        override fun onTick(millisUntilFinished: Long) {
            if (isAdded) tickHandler(millisUntilFinished)
        }

        override fun onFinish() {
            if (isAdded) finishHandler()
        }
    }
    cdt.start()
    return cdt
}
