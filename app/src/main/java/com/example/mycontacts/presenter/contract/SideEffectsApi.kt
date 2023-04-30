package com.example.mycontacts.presenter.contract

import android.content.DialogInterface
import android.net.Uri
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.example.mycontacts.domain.model.Contact

fun Fragment.sideEffects() = requireActivity() as SideEffectsApi

typealias PhotoPickerCallback = (Uri) -> Unit

interface SideEffectsApi {

    fun hasPermission(permission: String): Boolean

    fun requestPermission(permList: Array<String>, requestCode: Int)

    fun showToast(message: String)

    fun showConfirmDialog(
        @StringRes message: Int,
        negativeButtonListener: DialogInterface.OnClickListener?,
        positiveButtonListener: DialogInterface.OnClickListener?
    )

    fun startCall(contact: Contact)

    fun syncContacts(block: () -> Unit)

    fun pickPhoto(callback: PhotoPickerCallback)
}
