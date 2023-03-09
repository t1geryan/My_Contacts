package com.example.mycontacts.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact (
    var name: String,
    var number: String,
    var photo: String = "",
    var isFavorite: Boolean = false,
    var id: ULong = 0UL
) : Parcelable