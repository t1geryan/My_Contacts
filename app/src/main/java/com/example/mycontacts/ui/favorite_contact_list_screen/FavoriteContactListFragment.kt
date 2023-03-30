package com.example.mycontacts.ui.favorite_contact_list_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.ui.base.BaseContactListFragment
import com.example.mycontacts.ui.base.BaseContactListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteContactListFragment : BaseContactListFragment() {

    override val viewModel: BaseContactListViewModel by viewModels<FavoriteContactListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (viewModel as FavoriteContactListViewModel).contacts.observe(viewLifecycleOwner) {
            adapter.contacts = it
        }
    }

    override fun showContactInputDialog(contact: Contact) {
        val direction =
            FavoriteContactListFragmentDirections.actionFavoriteContactListFragmentToContactInputDialogFragment(
                contact
            )
        findNavController().navigate(direction)
    }
}

