package com.example.mycontacts.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile(
    val name: String = "",
    val number: String = "",
    val photo: String = ""
) : Parcelable
