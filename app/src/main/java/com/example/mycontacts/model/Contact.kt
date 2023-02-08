package com.example.mycontacts.model

data class Contact (
    val name: String,
    val number: String,
    val photo: String,
    val isFavorite: Boolean = false
)