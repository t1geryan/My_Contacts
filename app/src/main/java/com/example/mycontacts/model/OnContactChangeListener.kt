package com.example.mycontacts.model

interface OnContactChangeListener {
    fun onDetails(contact: Contact)

    fun onDeleteContact(contact: Contact)

    fun onChangeFavoriteStatus(contact: Contact)
}