package com.example.mycontacts.presenter.ui.base_contact_list

import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.domain.repository.ContactListRepository
import com.example.mycontacts.presenter.state.UiState
import com.example.mycontacts.presenter.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseContactListViewModel(
    protected val contactListRepository: ContactListRepository,
) : BaseViewModel() {

    private val _contacts = MutableStateFlow<UiState<List<Contact>>>(UiState.Loading())
    val contacts: StateFlow<UiState<List<Contact>>>
        get() = _contacts.asStateFlow()

    abstract val isOnlyFavoriteContacts: Boolean

    init {
        viewModelScope.launch {
            collectAsUiState(contactListRepository.getAllContacts(isOnlyFavoriteContacts), _contacts) {
                it.isEmpty()
            }
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
}