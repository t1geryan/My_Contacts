package com.example.mycontacts.ui.details

import androidx.annotation.StringRes

interface HasCustomTitleToolbar {

    @StringRes
    fun getTitle() : Int
}