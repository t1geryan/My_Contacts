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

    fun getName(): Flow<String> = flow {
        val name = preferences.getString(NAME_PREF, "") ?: throw IllegalStateException("NoPrefExistException")
        emit(name)
    }

    fun setName(name: String) {
        preferences.edit().putString(NAME_PREF, name).apply()
    }

    fun getNumber(): Flow<String> = flow {
        val number = preferences.getString(NUMBER_PREF, "") ?: throw IllegalStateException("NoPrefExistException")
        emit(number)
    }

    fun setNumber(number: String) {
        preferences.edit().putString(NAME_PREF, number).apply()
    }

    companion object {
        const val NAME_PREF = "NAME_PREF"
        const val NUMBER_PREF = "NUMBER_PREF"
    }
}