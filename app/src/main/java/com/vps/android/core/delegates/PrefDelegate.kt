package com.vps.android.core.delegates

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vps.android.core.local.PrefManager
import java.lang.reflect.Type
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PrefDelegate<T>(private val defaultValue: T) {
    private var storedValue: T? = null

    operator fun provideDelegate(
        thisRef: PrefManager,
        prop: KProperty<*>,
    ): ReadWriteProperty<PrefManager, T> {
        val key = prop.name

        return object : ReadWriteProperty<PrefManager, T> {
            override fun getValue(thisRef: PrefManager, property: KProperty<*>): T {
                if (!thisRef.preferences.contains(key)) {
                    setValue(thisRef, property, defaultValue)
                    return defaultValue
                }
                if (storedValue == null) {
                    @Suppress("UNCHECKED_CAST")
                    storedValue = when (defaultValue) {
                        is Int -> thisRef.preferences.getInt(key, defaultValue as Int) as T
                        is Long -> thisRef.preferences.getLong(key, defaultValue as Long) as T
                        is Float -> thisRef.preferences.getFloat(key, defaultValue as Float) as T
                        is String -> thisRef.preferences.getString(key, defaultValue as String) as T
                        is Boolean -> thisRef.preferences.getBoolean(key, defaultValue as Boolean) as T
                        else -> error("This type can not be stored into Preferences")
                    }
                }
                return storedValue!!
            }

            override fun setValue(thisRef: PrefManager, property: KProperty<*>, value: T) {
                with(thisRef.preferences.edit()) {
                    when (value) {
                        is String -> putString(key, value)
                        is Int -> putInt(key, value)
                        is Boolean -> putBoolean(key, value)
                        is Long -> putLong(key, value)
                        is Float -> putFloat(key, value)
                        else -> error("Only primitive types can be stored into Preferences")
                    }
                    apply()
                }
                storedValue = value
            }

        }
    }
}

class PrefObjDelegate<T>(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
    private val type: Type = object : TypeToken<T>() {}.type,
    private val key: String? = null,
    private val defaultValue: T? = null,
) : ReadWriteProperty<Any, T?> {

    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        val string = sharedPreferences.getString(getKey(property), null)
        return if (string.isNullOrEmpty()) {
            defaultValue
        } else {
            gson.fromJson(string, type)
        }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        val string = gson.toJson(value)
        sharedPreferences.edit()
            .putString(getKey(property), string)
            .apply()
    }

    private fun getKey(property: KProperty<*>) = key ?: property.name
}
