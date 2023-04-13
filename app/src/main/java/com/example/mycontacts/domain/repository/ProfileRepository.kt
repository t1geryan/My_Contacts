package com.example.mycontacts.domain.repository

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun getName() : Flow<String>

    suspend fun setName(name: String)

    suspend fun getNumber() : Flow<String>

    suspend fun setNumber(number: String)

    suspend fun getContactsCount() : Flow<Int>

    suspend fun getFavoriteContactsCount() : Flow<Int>
}