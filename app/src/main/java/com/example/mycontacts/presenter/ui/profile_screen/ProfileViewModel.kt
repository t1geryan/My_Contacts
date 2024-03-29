package com.example.mycontacts.presenter.ui.profile_screen

import com.example.mycontacts.domain.repository.ContactListRepository
import com.example.mycontacts.domain.repository.ProfileRepository
import com.example.mycontacts.presenter.state.UiState
import com.example.mycontacts.presenter.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _name = MutableStateFlow<UiState<String>>(UiState.Loading())
    val name: StateFlow<UiState<String>>
        get() = _name.asStateFlow()

    private val _number = MutableStateFlow<UiState<String>>(UiState.Loading())
    val number: StateFlow<UiState<String>>
        get() = _number.asStateFlow()

    private val _photo = MutableStateFlow<UiState<String>>(UiState.Loading())
    val photo: StateFlow<UiState<String>>
        get() = _photo.asStateFlow()

    private val _contactsCount = MutableStateFlow<UiState<Int>>(UiState.Loading())
    val contactsCount: StateFlow<UiState<Int>>
        get() = _contactsCount.asStateFlow()

    private val _favContactsCount = MutableStateFlow<UiState<Int>>(UiState.Loading())
    val favContactsCount: StateFlow<UiState<Int>>
        get() = _favContactsCount.asStateFlow()

    init {
        viewModelScope.launch {
            collectAsUiState(profileRepository.getName(), _name) {
                it.isBlank()
            }
            collectAsUiState(profileRepository.getPhoto(), _photo) {
                it.isBlank()
            }
            collectAsUiState(profileRepository.getNumber(), _number) {
                it.isBlank()
            }
            collectAsUiState(contactListRepository.getContactsCount(), _contactsCount) {
                it == 0
            }
            collectAsUiState(contactListRepository.getContactsCount(true), _favContactsCount) {
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

    fun syncContacts() = viewModelScope.launch {
        contactListRepository.syncContacts()
    }
}