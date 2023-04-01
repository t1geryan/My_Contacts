package com.example.mycontacts.ui.favorite_contact_list_screen

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.ui.base_contact_list.BaseContactListFragment
import com.example.mycontacts.ui.base_contact_list.BaseContactListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteContactListFragment : BaseContactListFragment() {

    override val viewModel: BaseContactListViewModel by viewModels<FavoriteContactListViewModel>()

    override fun showContactInputDialog(contact: Contact) {
        val direction =
            FavoriteContactListFragmentDirections.actionFavoriteContactListFragmentToContactInputDialogFragment(
                contact
            )
        findNavController().navigate(direction)
    }
}

