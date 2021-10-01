package com.vps.android.core.ext

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import java.util.concurrent.TimeUnit

fun Context.getResColor(@ColorRes colorId: Int): Int =
    ContextCompat.getColor(this, colorId)

val Long.mmSs: String
    get() = String.format(
        "%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(this).toInt() % 60,
        TimeUnit.MILLISECONDS.toSeconds(this).toInt() % 60
    )
