package com.example.mycontacts.ui.contact_list_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.domain.repository.ContactListRepository
import com.example.mycontacts.ui.base.BaseContactListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    contactListRepository: ContactListRepository, savedStateHandle: SavedStateHandle
) : BaseContactListViewModel(contactListRepository) {
    private val _contacts =
        savedStateHandle.getLiveData(KEY_CONTACT_LIST, contactListRepository.contacts)
    val contacts: LiveData<List<Contact>>
        get() = _contacts

    private val listener = contactListRepository.OnContactListChangeListener {
        _contacts.value = it
    }

    init {
        contactListRepository.addListener(listener)
    }

    override fun onCleared() {
        super.onCleared()
        contactListRepository.removeListener(listener)
    }

    companion object {
        @JvmStatic
        private val KEY_CONTACT_LIST = "KEY_CONTACT_LIST"
    }
}