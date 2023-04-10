package com.example.mycontacts.ui.contact_list_screen

import androidx.lifecycle.viewModelScope
import com.example.mycontacts.domain.repository.ContactListRepository
import com.example.mycontacts.ui.base_contact_list.BaseContactListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    contactListRepository: ContactListRepository
) : BaseContactListViewModel(contactListRepository) {

    fun deleteAllContacts() = viewModelScope.launch {
        contactListRepository.deleteAllContacts()
    }

    fun syncContacts() = viewModelScope.launch {
        contactListRepository.syncContacts()
    }

    override val isOnlyFavoriteContacts: Boolean = false
}