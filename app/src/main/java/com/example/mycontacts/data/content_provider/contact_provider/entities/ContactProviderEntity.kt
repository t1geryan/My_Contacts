package com.example.mycontacts.data.content_provider.contact_provider.entities

import com.example.mycontacts.data.database.contact_database.entities.ContactEntity

data class ContactProviderEntity(
    val name: String,
    val number: String,
    val photoUri: String?
) {

    fun toContactEntity() : ContactEntity = ContactEntity(
        id = 0,
        name = name,
        number = number,
        photo = photoUri,
        isFavorite = false
    )
}