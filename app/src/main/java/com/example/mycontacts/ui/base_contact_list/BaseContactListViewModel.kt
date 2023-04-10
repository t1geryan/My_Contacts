package com.example.mycontacts.ui.base_contact_list

import androidx.lifecycle.ViewModel
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.domain.model.Result
import com.example.mycontacts.domain.repository.ContactListRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseContactListViewModel(
    protected val contactListRepository: ContactListRepository,
) : ViewModel() {

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _contacts = MutableStateFlow<Result<List<Contact>>>(Result.Loading())
    val contacts: StateFlow<Result<List<Contact>>>
        get() = _contacts.asStateFlow()

    abstract val isOnlyFavoriteContacts: Boolean

    init {
        viewModelScope.launch {
            fetchCurrentContactList()
        }
    }

    fun addContact(contact: Contact) = viewModelScope.launch {
        contactListRepository.addContact(contact)
    }

    fun deleteContact(contact: Contact) = viewModelScope.launch {
        contactListRepository.deleteContact(contact)
    }

    fun changeContactFavoriteStatus(contact: Contact) = viewModelScope.launch {
        contactListRepository.changeContactFavoriteStatus(contact)
    }

    fun updateContact(contact: Contact) = viewModelScope.launch {
        contactListRepository.changeContactData(contact)
    }

    private fun fetchCurrentContactList() = viewModelScope.launch {
        _contacts.value = Result.Loading()
        try {
            contactListRepository.getAllContacts(onlyFavorites = isOnlyFavoriteContacts)
                .collect { list ->
                    _contacts.value = if (list.isEmpty()) Result.EmptyOrNull()
                    else Result.Success(list)
                }
        } catch (e: Exception) {
            _contacts.value = Result.Error(e.message)
        }
    }
}