package com.example.mycontacts.domain.repository

import com.example.mycontacts.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactListRepository {
    fun getContacts(): Flow<List<Contact>>

    fun getFavoriteContacts(): Flow<List<Contact>>

    fun addContact(contact: Contact)

    fun deleteContact(contact: Contact)

    fun changeContactFavoriteStatus(contact: Contact)

    fun changeContactData(oldContact: Contact, newContact: Contact)
}