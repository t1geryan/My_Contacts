package com.example.mycontacts.view.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface HasCustomActionToolbar {

    fun getCustomAction() : Action
}

class Action(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    val onAction : Runnable
)