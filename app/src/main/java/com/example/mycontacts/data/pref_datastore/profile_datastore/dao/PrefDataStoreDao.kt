package com.example.mycontacts.data.pref_datastore.profile_datastore.dao

import kotlinx.coroutines.flow.Flow

interface PrefDataStoreDao {

    suspend fun getName() : Flow<String>

    suspend fun setName(value: String)

    suspend fun getNumber() : Flow<String>

    suspend fun setNumber(value: String)

    suspend fun getPhoto() : Flow<String>

    suspend fun setPhoto(value: String)

}