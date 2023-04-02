package com.example.mycontacts.ui.base_contact_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.domain.repository.ContactListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseContactListViewModel(
    protected val contactListRepository: ContactListRepository,
) : ViewModel() {

    protected val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>>
        get() = _contacts.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            fetchCurrentContactList()
        }
    }

    fun addContact(contact: Contact) = viewModelScope.launch(Dispatchers.Default) {
        contactListRepository.addContact(contact)
    }

    fun deleteContact(contact: Contact) = viewModelScope.launch(Dispatchers.Default) {
        contactListRepository.deleteContact(contact)
    }

    fun changeContactFavoriteStatus(contact: Contact) = viewModelScope.launch(Dispatchers.Default) {
        contactListRepository.changeContactFavoriteStatus(contact)
    }

    abstract suspend fun fetchCurrentContactList()
}