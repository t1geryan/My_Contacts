package com.example.mycontacts.ui.profile_screen

import com.example.mycontacts.domain.model.Result
import com.example.mycontacts.domain.repository.ContactListRepository
import com.example.mycontacts.domain.repository.ProfileRepository
import com.example.mycontacts.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val contactListRepository: ContactListRepository,
    private val profileRepository: ProfileRepository
) : BaseViewModel() {

    private val _name = MutableStateFlow<Result<String>>(Result.Loading())
    val name: StateFlow<Result<String>>
        get() = _name.asStateFlow()

    private val _number = MutableStateFlow<Result<String>>(Result.Loading())
    val number: StateFlow<Result<String>>
        get() = _number.asStateFlow()

    private val _photo = MutableStateFlow<Result<String>>(Result.Loading())
    val photo: StateFlow<Result<String>>
        get() = _number.asStateFlow()

    private val _contactsCount = MutableStateFlow<Result<Int>>(Result.Loading())
    val contactsCount: StateFlow<Result<Int>>
        get() = _contactsCount.asStateFlow()

    private val _favContactsCount = MutableStateFlow<Result<Int>>(Result.Loading())
    val favContactsCount: StateFlow<Result<Int>>
        get() = _favContactsCount.asStateFlow()

    init {
        viewModelScope.launch {
            fetchAsync(profileRepository.getName(), _name) {
                it.isBlank()
            }
            fetchAsync(profileRepository.getPhoto(), _photo) {
                it.isBlank()
            }
            fetchAsync(profileRepository.getNumber(), _number) {
                it.isBlank()
            }
            fetchAsync(contactListRepository.getContactsCount(), _contactsCount) {
                it == 0
            }
            fetchAsync(contactListRepository.getContactsCount(true), _favContactsCount) {
                it == 0
            }
        }
    }

    fun setName(name: String) = viewModelScope.launch {
        profileRepository.setName(name)
    }

    fun setNumber(number: String) = viewModelScope.launch {
        profileRepository.setNumber(number)
    }

    fun setPhotoUri(uri: String) = viewModelScope.launch {
        profileRepository.setPhoto(uri)
    }

    private fun <T> fetchAsync(
        getData: Flow<T>,
        stateFlow: MutableStateFlow<Result<T>>,
        isEmptyOrNull: (T) -> Boolean
    ) = viewModelScope.launch {
        stateFlow.value = Result.Loading()
        try {
            getData.collect { data ->
                stateFlow.value = if (isEmptyOrNull(data)) {
                    Result.EmptyOrNull()
                } else {
                    Result.Success(data)
                }
            }
        } catch (e: Exception) {
            stateFlow.value = Result.Error(e.message)
        }
    }

    fun syncContacts() = viewModelScope.launch {
        contactListRepository.syncContacts()
    }
}