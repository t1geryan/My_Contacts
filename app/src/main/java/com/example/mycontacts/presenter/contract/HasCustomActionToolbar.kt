package com.example.mycontacts.presenter.contract

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface HasCustomActionToolbar {

    fun getCustomActionsList(): List<Action>
}

class Action(
    @DrawableRes val icon: Int, @StringRes val title: Int, val onAction: Runnable
)
