package com.wynneplaga.beyondfilm

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author krzysztof.kosobudzki
 */
private const val APP_PREF_NAME = "beyondCovid"

private class PreferenceProperty<T>(
    private val key: String,
    private val defaultValue: T,
    private val getter: SharedPreferences.(String, T) -> T,
    private val setter: SharedPreferences.Editor.(String, T) -> SharedPreferences.Editor
) : ReadWriteProperty<Context, T> {

    override fun getValue(thisRef: Context, property: KProperty<*>): T =
        thisRef.getPreferences()
            .getter(key, defaultValue)

    override fun setValue(thisRef: Context, property: KProperty<*>, value: T) =
        thisRef.getPreferences()
            .edit()
            .setter(key, value)
            .apply()

    private fun Context.getPreferences(): SharedPreferences =
        getSharedPreferences(APP_PREF_NAME, Context.MODE_PRIVATE)
}

fun intPreference(key: String, defaultValue: Int = 0) : ReadWriteProperty<Context, Int> =
    PreferenceProperty(
        key = key,
        defaultValue = defaultValue,
        getter = SharedPreferences::getInt,
        setter = SharedPreferences.Editor::putInt
    )

fun booleanPreference(key: String, defaultValue: Boolean = false) : ReadWriteProperty<Context, Boolean> =
    PreferenceProperty(
        key = key,
        defaultValue = defaultValue,
        getter = SharedPreferences::getBoolean,
        setter = SharedPreferences.Editor::putBoolean
    )

fun stringPreference(key: String, defaultValue: String? = null) : ReadWriteProperty<Context, String?> =
    PreferenceProperty(
        key = key,
        defaultValue = defaultValue,
        getter = SharedPreferences::getString,
        setter = SharedPreferences.Editor::putString
    )