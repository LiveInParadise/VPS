package com.vps.android.core.ext

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.vps.android.core.delegates.FragmentViewBindingDelegate
import java.text.SimpleDateFormat
import java.util.*

fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T) =
    FragmentViewBindingDelegate(this, viewBindingFactory)

fun Fragment.hideKeyboard() {
    view?.hideKeyboard()
}

fun Date.toRequestFormat(): String =
    SimpleDateFormat("y-M-dd HH:mm:ss", Locale.getDefault()).format(this)
