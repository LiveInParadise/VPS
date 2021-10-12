package com.vps.android.core.utils

import java.util.concurrent.TimeUnit

object CommonUtils {

    fun convertMsToTime(timeInMs: Long) =
        String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeInMs),
            TimeUnit.MILLISECONDS.toMinutes(timeInMs) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInMs)),
            TimeUnit.MILLISECONDS.toSeconds(timeInMs) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMs)))
}
