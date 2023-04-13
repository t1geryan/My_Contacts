package com.example.mycontacts.data.repostory

import com.example.mycontacts.data.database.contact_database.dao.ContactDao
import com.example.mycontacts.data.shared_preferences.ProfilePrefHelper
import com.example.mycontacts.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profilePrefHelper: ProfilePrefHelper, private val contactDao: ContactDao
) : ProfileRepository {

    override suspend fun getName(): Flow<String> = profilePrefHelper.getName()

    override suspend fun setName(name: String) = profilePrefHelper.setName(name)

    override suspend fun getNumber(): Flow<String> = profilePrefHelper.getNumber()

    override suspend fun setNumber(number: String) = profilePrefHelper.setNumber(number)

    override suspend fun getContactsCount(): Flow<Int> = contactDao.getAllContacts().map {
        it.size
    }

    override suspend fun getFavoriteContactsCount(): Flow<Int> = contactDao.getAllContacts().map {
        it.size
    }
}