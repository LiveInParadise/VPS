package com.vps.android.core.ext

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.viewbinding.ViewBinding
import com.google.android.material.textfield.TextInputLayout
import com.vps.android.core.delegates.CustomViewBindingDelegate

fun <T : ViewBinding> View.viewBinding(
    viewBindingFactory: (LayoutInflater, ViewGroup) -> T,
    ignoreLifecycle: Boolean = false,
) = CustomViewBindingDelegate(this, viewBindingFactory, ignoreLifecycle)

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun EditText.hideKeyboardOnOutClick() {
    setOnFocusChangeListener { v, hasFocus ->
        if (!hasFocus) v.hideKeyboard()
    }
}

inline fun View.waitForLayout(crossinline f: () -> Unit) = with(viewTreeObserver) {
    addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            f()
        }
    })
}

fun View.visible(visible: Boolean, hide: Boolean = false) {
    this.visibility = if (visible) View.VISIBLE else if (hide) View.INVISIBLE else View.GONE
}

fun TextInputLayout.setError(isError: Boolean, errorText: String? = null) {
    if (isError) {
        error = errorText
        isErrorEnabled = true
    } else {
        error = null
        isErrorEnabled = false
    }
}
