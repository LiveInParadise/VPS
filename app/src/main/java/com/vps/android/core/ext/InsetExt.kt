package com.vps.android.core.ext

import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnAttach

fun View.doOnApplyWindowInsets(listener: (View, WindowInsetsCompat) -> WindowInsetsCompat) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        listener(view, insets)
    }
    requestApplyInsetsWhenAttached()
}

fun View.clearInsetsListener() {
    ViewCompat.setOnApplyWindowInsetsListener(this, null)
}

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        doOnAttach {
            requestApplyInsets()
        }
    }
}

fun WindowInsetsCompat.updateSystemWindowInsets(
    left: Int = systemWindowInsets.left,
    top: Int = systemWindowInsets.top,
    right: Int = systemWindowInsets.right,
    bottom: Int = systemWindowInsets.bottom
): WindowInsetsCompat {
    return WindowInsetsCompat
        .Builder(this)
        .setSystemWindowInsets(
            Insets.of(left, top, right, bottom)
        )
        .build()
}
