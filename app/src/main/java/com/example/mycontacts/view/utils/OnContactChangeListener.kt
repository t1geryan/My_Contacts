package com.example.mycontacts.view.utils

import com.example.mycontacts.model.Contact

interface OnContactChangeListener {
    fun onDeleteContact(contact: Contact)

    fun onChangeFavoriteStatus(contact: Contact)
}