package com.example.mycontacts.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val id: Long = 0,
    var name: String = "",
    var number: String = "",
    var photo: String = "",
    var isFavorite: Boolean = false,
) : Parcelable, Comparable<Contact> {
    override fun compareTo(other: Contact): Int {
        var result = 1
        if (name.lowercase() < other.name.lowercase()) result = -1
        else if (id == other.id) result = 0
        return result
    }

}