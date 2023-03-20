package com.example.mycontacts.domain.model

interface OnContactChangeListener {
    fun onDelete(contact: Contact)

    fun onChangeData(contact: Contact)

    fun onChangeFavoriteStatus(contact: Contact)

    fun onCall(contact: Contact)
}