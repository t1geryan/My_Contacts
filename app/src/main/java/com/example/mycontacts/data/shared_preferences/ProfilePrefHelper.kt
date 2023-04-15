package com.example.mycontacts.data.shared_preferences

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfilePrefHelper @Inject constructor(
    private val preferences: SharedPreferences
) {
    fun setStringPref(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun getStringPref(key: String) : Flow<String>  = flow {
        val result = preferences.getString(key, "") ?: throw IllegalStateException("NoPrefExistException")
        emit(result)
    }

    companion object {
        const val NAME_PREF = "NAME_PREF"
        const val NUMBER_PREF = "NUMBER_PREF"
        const val PHOTO_PREF = "PHOTO_PREF"
    }
}