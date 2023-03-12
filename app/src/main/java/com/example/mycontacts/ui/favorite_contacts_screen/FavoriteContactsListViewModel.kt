package com.example.mycontacts.ui.favorite_contacts_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.domain.repository.ContactListRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteContactsListViewModel @Inject constructor(
    private val contactListRepository: ContactListRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _contacts = savedStateHandle.getLiveData(KEY_FAVORITE_CONTACT_LIST, contactListRepository.getFavoriteContactsList())
    val contacts : LiveData<List<Contact>>
        get() = _contacts

    private val listener = contactListRepository.OnContactListChangeListener{
        _contacts.value = contactListRepository.getFavoriteContactsList()
    }

    init {
        contactListRepository.addListener(listener)
    }

    override fun onCleared() {
        super.onCleared()
        contactListRepository.removeListener(listener)
    }

    fun changeContactFavoriteStatus(contact: Contact) {
        contactListRepository.changeContactFavoriteStatus(contact)
    }

    companion object {
        @JvmStatic
        private val KEY_FAVORITE_CONTACT_LIST = "KEY_FAVORITE_CONTACT_LIST"
    }
}