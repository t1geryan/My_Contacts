package com.example.mycontacts.presenter.ui.favorite_contact_list_screen

import com.example.mycontacts.domain.repository.ContactListRepository
import com.example.mycontacts.presenter.ui.base_contact_list.BaseContactListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteContactListViewModel @Inject constructor(
    contactListRepository: ContactListRepository
) : BaseContactListViewModel(contactListRepository) {

    override val isOnlyFavoriteContacts: Boolean = true
}