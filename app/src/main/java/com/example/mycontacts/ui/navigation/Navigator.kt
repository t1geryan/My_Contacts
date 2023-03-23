package com.example.mycontacts.ui.navigation

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.mycontacts.domain.model.Contact

fun Fragment.navigator() = (requireActivity() as Navigator)

interface Navigator {
    fun launchContactListScreen()

    fun launchFavoriteContactsScreen()

    fun launchContactInputScreen(contact: Contact = Contact())

    fun showToast(message : String)

    fun startCall(contact: Contact)

    fun <T: Parcelable> publishResult(result: T)

    fun <T: Parcelable> listenResult(clazz: Class<T>, owner : LifecycleOwner, listener: (T)->Unit)

    fun requestPermission(permList : Array<String>, requestCode : Int)
}