package com.example.mycontacts.domain.mapper

import com.example.mycontacts.data.database.contact_database.entities.ContactEntity
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.utils.Mapper

class ContactMapper : Mapper<Contact, ContactEntity> {
    override fun domainToEntity(domain: Contact): ContactEntity = with(domain) {
        ContactEntity(
            0,
            name,
            number,
            photo,
            isFavorite
        )
    }

    override fun entityToDomain(entity: ContactEntity): Contact = with(entity) {
        Contact(
            name,
            number,
            photo ?: "",
            isFavorite,
            id.toULong()
        )
    }


}