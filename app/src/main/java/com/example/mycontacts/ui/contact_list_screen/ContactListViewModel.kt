package com.example.mycontacts.ui.contact_list_screen

import com.example.mycontacts.domain.repository.ContactListRepository
import com.example.mycontacts.ui.base_contact_list.BaseContactListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    contactListRepository: ContactListRepository
) : BaseContactListViewModel(contactListRepository) {

    override suspend fun fetchCurrentContactsList() {
        contactListRepository.getAllContacts().collect {
            _contacts.value = it
        }
    }
}