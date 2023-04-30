package com.example.mycontacts.presenter.ui.favorite_contact_list_screen

import androidx.fragment.app.viewModels
import com.example.mycontacts.R
import com.example.mycontacts.presenter.ui.base_contact_list.BaseContactListFragment
import com.example.mycontacts.presenter.ui.base_contact_list.BaseContactListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteContactListFragment : BaseContactListFragment() {

    override val viewModel: BaseContactListViewModel by viewModels<FavoriteContactListViewModel>()

    override fun getEmptyListMessage(): Int = R.string.no_favorite_contacts_message
}

