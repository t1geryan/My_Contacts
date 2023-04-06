package com.example.mycontacts.data.content_provider.contact_provider.dao

import com.example.mycontacts.data.content_provider.contact_provider.entities.ContactProviderEntity

interface ContactProviderDao {

    suspend fun getAllContacts() : List<ContactProviderEntity>
}