package com.example.mycontacts.domain.model

/*
    due to the presence of empty functions,
     the implementation will be able to determine only those listeners
      that are needed for a particular screen
 */
interface OnContactChangeListener {
    fun onDeleteContact(contact: Contact) {

    }

    fun onChangeFavoriteStatus(contact: Contact) {

    }
}