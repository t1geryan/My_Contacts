package com.example.mycontacts.domain.mapper

import com.example.mycontacts.data.database.contact_database.entities.ContactEntity
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.utils.Mapper
import javax.inject.Inject

class ContactMapper @Inject constructor() : Mapper<Contact, ContactEntity> {
    override fun domainToEntity(domain: Contact): ContactEntity = with(domain) {
        ContactEntity(
            id = id, name = name, number = number, photo = photo, isFavorite = isFavorite
        )
    }

    override fun entityToDomain(entity: ContactEntity): Contact = with(entity) {
        Contact(
            id = id,
            name = name,
            number = number,
            photo = photo ?: "",
            isFavorite = isFavorite,
        )
    }
}