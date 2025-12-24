package com.pam.uas.data.prefs

import android.content.Context

object PinManager {

    private const val PREF_NAME = "parental_pin"
    private const val KEY_PIN = "pin"

    fun savePin(context: Context, pin: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_PIN, pin).apply()
    }

    fun getPin(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_PIN, null)
    }

    fun isPinExists(context: Context): Boolean {
        return getPin(context) != null
    }

    fun checkPin(context: Context, inputPin: String): Boolean {
        return getPin(context) == inputPin
    }
}
