package com.example.mycontacts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.mycontacts.model.Contact
import com.example.mycontacts.model.ContactListRepository

class ContactListViewModel(
    private val contactListRepository: ContactListRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts : LiveData<List<Contact>>
        get() = _contacts

    private val listener = contactListRepository.OnContactListChangeListener{
        _contacts.value = it
    }

    init {
        _contacts.value = contactListRepository.contacts

        contactListRepository.addListener(listener)

    }

    override fun onCleared() {
        super.onCleared()
        contactListRepository.removeListener(listener)
    }

    fun addContact(contact: Contact) {
        contactListRepository.addContact(contact)
    }

    fun deleteContact(contact: Contact) {
        contactListRepository.deleteContact(contact)
    }

    fun changeContactFavoriteStatus(contact: Contact) {
        contactListRepository.changeContactFavoriteStatus(contact)
    }


}