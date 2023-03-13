package com.example.mycontacts.ui.navigation

import androidx.fragment.app.Fragment

fun Fragment.navigator() = (requireActivity() as Navigator)

interface Navigator {
    fun launchContactListScreen()

    fun launchFavoriteContactsScreen()

    fun showToast(message : String)
}