package com.example.mycontacts.domain.repository

import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface ContactListRepository {
    suspend fun getAllContacts(onlyFavorites: Boolean): Flow<Result<List<Contact>>>

    suspend fun addContact(contact: Contact)

    suspend fun deleteContact(contact: Contact)

    suspend fun deleteAllContacts()

    suspend fun changeContactFavoriteStatus(contact: Contact)

    // modified contact saves previous id
    suspend fun changeContactData(newContact: Contact)

    suspend fun syncContacts()
}