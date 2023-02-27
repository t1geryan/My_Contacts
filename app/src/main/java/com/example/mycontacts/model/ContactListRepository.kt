package com.example.mycontacts.model

abstract class ContactListRepository {

    abstract fun getContact(position: Int): Contact

    abstract fun findFirst(contact: Contact): Int

    abstract fun addContact(contact: Contact)

    abstract fun deleteContact(contact: Contact)

    abstract fun changeContactFavoriteStatus(contact: Contact)
}