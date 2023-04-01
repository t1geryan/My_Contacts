package com.example.mycontacts.domain.repository

import com.example.mycontacts.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactListRepository {
    suspend fun getAllContacts(): Flow<List<Contact>>

    suspend fun getFavoriteContacts(): Flow<List<Contact>>

    suspend fun addContact(contact: Contact)

    suspend fun deleteContact(contact: Contact)

    suspend fun changeContactFavoriteStatus(contact: Contact)

    // modified contact saves previous id
    suspend fun changeContactData(newContact: Contact)
}