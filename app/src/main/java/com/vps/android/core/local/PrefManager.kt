package com.vps.android.core.local

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit
import com.google.gson.Gson
import com.vps.android.core.delegates.PrefDelegate

class PrefManager(
    context: Context,
    gson: Gson
) {

    internal val preferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    var token by PrefDelegate("")
    var deviceId by PrefDelegate("a4a9498asd498asd489")


    fun clearAll() {
        preferences.edit { clear() }
    }
}
