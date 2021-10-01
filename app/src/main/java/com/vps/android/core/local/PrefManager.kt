package com.vps.android.core.local

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit
import com.squareup.moshi.Moshi
import com.vps.android.core.delegates.PrefDelegate
import java.util.*

class PrefManager(
    context: Context,
    moshi: Moshi
) {

    internal val preferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    var accessToken by PrefDelegate("")
    var deviceToken by PrefDelegate(generateDeviceToken())


    fun clearAll() {
        preferences.edit { clear() }
    }

    private fun generateDeviceToken() = UUID.randomUUID().toString().substring(0, 32)
}
