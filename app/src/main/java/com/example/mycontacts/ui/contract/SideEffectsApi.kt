package com.example.mycontacts.ui.contract

import androidx.fragment.app.Fragment
import com.example.mycontacts.domain.model.Contact

fun Fragment.sideEffectsHolder() = requireActivity() as SideEffectsApi

interface SideEffectsApi {

    fun requestPermission(permList: Array<String>, requestCode: Int)

    fun showToast(message: String)

    fun startCall(contact: Contact)
}