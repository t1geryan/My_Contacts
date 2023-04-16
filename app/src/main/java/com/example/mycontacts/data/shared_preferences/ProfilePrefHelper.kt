package com.example.mycontacts.data.shared_preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mycontacts.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ProfilePrefHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val Context.dataStore by preferencesDataStore(
        name = Constants.PROFILE_PREFERENCES_NAME
    )

    suspend fun setStringPref(key: Preferences.Key<String>, value: String) {
        context.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }

    fun getStringPref(key: Preferences.Key<String>): Flow<String> =
        context.dataStore.data
            .map { pref ->
                pref[key] ?: ""
            }


    companion object {
        val NAME_PREF = stringPreferencesKey("NAME_PREF")
        val NUMBER_PREF = stringPreferencesKey("NUMBER_PREF")
        val PHOTO_PREF = stringPreferencesKey("PHOTO_PREF")
    }
}