package com.example.mycontacts.data.pref_datastore.profile_datastore.datastore

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mycontacts.data.pref_datastore.dataStore
import com.example.mycontacts.data.pref_datastore.getValue
import com.example.mycontacts.data.pref_datastore.profile_datastore.dao.PrefDataStoreDao
import com.example.mycontacts.data.pref_datastore.setValue
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PrefDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) : PrefDataStoreDao {

    private val dataStore = context.dataStore

    companion object {
        val NAME_PREF = stringPreferencesKey("NAME_PREF")
        val NUMBER_PREF = stringPreferencesKey("NUMBER_PREF")
        val PHOTO_PREF = stringPreferencesKey("PHOTO_PREF")
    }

    override suspend fun getName(): Flow<String> = dataStore.getValue(NAME_PREF, "")

    override suspend fun setName(value: String) = dataStore.setValue(NAME_PREF, value)

    override suspend fun getNumber(): Flow<String> = dataStore.getValue(NUMBER_PREF, "")

    override suspend fun setNumber(value: String) = dataStore.setValue(NUMBER_PREF, value)

    override suspend fun getPhoto(): Flow<String> = dataStore.getValue(PHOTO_PREF, "")

    override suspend fun setPhoto(value: String) = dataStore.setValue(PHOTO_PREF, value)
}