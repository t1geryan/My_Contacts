package com.example.mycontacts.data.repostory

import com.example.mycontacts.data.shared_preferences.ProfilePrefHelper
import com.example.mycontacts.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profilePrefHelper: ProfilePrefHelper
) : ProfileRepository {
    override suspend fun getName(): Flow<String> =
        profilePrefHelper.getStringPref(ProfilePrefHelper.NAME_PREF)

    override suspend fun setName(name: String) =
        profilePrefHelper.setStringPref(ProfilePrefHelper.NAME_PREF, name)

    override suspend fun getNumber(): Flow<String> =
        profilePrefHelper.getStringPref(ProfilePrefHelper.NUMBER_PREF)

    override suspend fun setNumber(number: String) =
        profilePrefHelper.setStringPref(ProfilePrefHelper.NUMBER_PREF, number)

    override suspend fun getPhoto(): Flow<String> =
        profilePrefHelper.getStringPref(ProfilePrefHelper.PHOTO_PREF)

    override suspend fun setPhoto(photo: String) =
        profilePrefHelper.setStringPref(ProfilePrefHelper.PHOTO_PREF, photo)
}