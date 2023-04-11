package com.example.mycontacts.ui.input_contact_screen

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycontacts.domain.model.Contact
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ContactInputViewModel @AssistedInject constructor(
    @Assisted prevContact: Contact
) : ViewModel() {
    val photo = MutableLiveData<Uri?>()

    val name = MutableLiveData<String>()

    val number = MutableLiveData<String>()

    init {
        photo.value = with(prevContact.photo) {
            if (isNotEmpty())
                toUri()
            else
                null
        }
        name.value = prevContact.name
        number.value = prevContact.number
    }

    @AssistedFactory
    interface Factory {
        fun create(contact: Contact): ContactInputViewModel
    }
}