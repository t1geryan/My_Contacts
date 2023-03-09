package com.example.mycontacts.domain.model

interface OnContactChangeListener {
    fun onDeleteContact(contact: Contact)

    fun onChangeFavoriteStatus(contact: Contact)
}