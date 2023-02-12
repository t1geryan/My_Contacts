package com.example.mycontacts.view.utils

import androidx.annotation.StringRes

interface HasCustomTitleToolbar {

    @StringRes
    fun getTitle() : Int
}