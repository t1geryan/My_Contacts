package com.example.mycontacts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.mycontacts.model.Contact
import com.example.mycontacts.model.ContactListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    private val contactListRepository: ContactListRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(){
    private val _contacts = savedStateHandle.getLiveData(KEY_CONTACT_LIST, contactListRepository.contacts)
    val contacts : LiveData<List<Contact>>
        get() = _contacts

    private val listener = contactListRepository.OnContactListChangeListener{
        _contacts.value = it
    }

    init {
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

    companion object {
        @JvmStatic
        private val KEY_CONTACT_LIST = "KEY_CONTACT_LIST"
    }
}