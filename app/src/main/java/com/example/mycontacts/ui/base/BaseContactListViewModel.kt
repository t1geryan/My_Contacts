package com.example.mycontacts.ui.base

import androidx.lifecycle.ViewModel
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.domain.repository.ContactListRepository

abstract class BaseContactListViewModel protected constructor(
    protected val contactListRepository: ContactListRepository,
) : ViewModel() {

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