package com.vps.android.core.local

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.provider.Settings
import androidx.core.content.edit
import com.google.gson.Gson
import com.vps.android.core.delegates.PrefDelegate
import com.vps.android.core.delegates.PrefObjDelegate
import com.vps.android.domain.mechanism.MechanismItem
import com.vps.android.domain.mechanism.MechanismType

class PrefManager(
    private val context: Context,
    gson: Gson,
) {

    internal val preferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    var token by PrefDelegate("")

//    var deviceId by PrefDelegate(getUniqueDeviceId())
    var deviceId by PrefDelegate("a4a9498asd498asd489")
    var showDeviceId by PrefDelegate(true)

    var userMechanismType: MechanismType? by PrefObjDelegate(preferences, gson, MechanismType::class.java, defaultValue = null)
    var userMechanism: MechanismItem? by PrefObjDelegate(preferences, gson, MechanismItem::class.java, defaultValue = null)

    private fun getUniqueDeviceId(): String = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    fun clearAll() {
        val showDeviceIdCache = showDeviceId
        preferences.edit { clear() }
        showDeviceId = showDeviceIdCache
    }
}
