package com.example.mycontacts.data.repostory

import com.example.mycontacts.data.pref_datastore.profile_datastore.dao.PrefDataStoreDao
import com.example.mycontacts.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val prefDataStoreDao: PrefDataStoreDao
) : ProfileRepository {
    override suspend fun getName(): Flow<String> = prefDataStoreDao.getName()

    override suspend fun setName(name: String) = prefDataStoreDao.setName(name)

    override suspend fun getNumber(): Flow<String> = prefDataStoreDao.getNumber()

    override suspend fun setNumber(number: String) = prefDataStoreDao.setNumber(number)

    override suspend fun getPhoto(): Flow<String> = prefDataStoreDao.getPhoto()

    override suspend fun setPhoto(photo: String) = prefDataStoreDao.setPhoto(photo)

}