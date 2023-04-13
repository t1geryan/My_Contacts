package com.example.mycontacts.ui.profile_screen

import com.example.mycontacts.domain.model.Result
import com.example.mycontacts.domain.repository.ProfileRepository
import com.example.mycontacts.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : BaseViewModel() {

    private val _name = MutableStateFlow<Result<String>>(Result.Loading())
    val name: StateFlow<Result<String>>
        get() = _name.asStateFlow()

    private val _number = MutableStateFlow<Result<String>>(Result.Loading())
    val number: StateFlow<Result<String>>
        get() = _number.asStateFlow()

    private val _contactsCount = MutableStateFlow<Result<Int>>(Result.Loading())
    val contactsCount: StateFlow<Result<Int>>
        get() = _contactsCount.asStateFlow()

    private val _favContactsCount = MutableStateFlow<Result<Int>>(Result.Loading())
    val favContactsCount: StateFlow<Result<Int>>
        get() = _contactsCount.asStateFlow()

    init {
        viewModelScope.launch {
            _name.value = fetchAsync(profileRepository.getName()) {
                it.isBlank()
            }.await()
            _number.value = fetchAsync(profileRepository.getNumber()) {
                it.isBlank()
            }.await()
            _contactsCount.value = fetchAsync(profileRepository.getContactsCount()) {
                it == 0
            }.await()
            _favContactsCount.value = fetchAsync(profileRepository.getFavoriteContactsCount()) {
                it == 0
            }.await()
        }
    }

    fun setName(name: String) = viewModelScope.launch {
        profileRepository.setName(name)
    }

    fun setNumber(number: String) = viewModelScope.launch {
        profileRepository.setNumber(number)
    }

    private fun <T> fetchAsync(
        getData: Flow<T>,
        isEmptyOrNull: (T) -> Boolean
    ): Deferred<Result<T>> = viewModelScope.async {
        var result: Result<T> = Result.Loading()
        try {
            getData.collect { data ->
                result = if (isEmptyOrNull(data)) {
                    Result.EmptyOrNull()
                } else {
                    Result.Success(data)
                }
            }
        } catch (e: Exception) {
            result = Result.Error(e.message)
        }
        result
    }
}