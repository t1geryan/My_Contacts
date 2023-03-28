package com.example.mycontacts.ui.contact_list_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mycontacts.R
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.ui.base.BaseContactListFragment
import com.example.mycontacts.ui.base.BaseContactListViewModel
import com.example.mycontacts.ui.contract.Action
import com.example.mycontacts.ui.contract.HasCustomActionToolbar
import com.example.mycontacts.ui.contract.fragmentResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactListFragment : BaseContactListFragment(), HasCustomActionToolbar {

    override val viewModel: BaseContactListViewModel by viewModels<ContactListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (viewModel as ContactListViewModel).contacts.observe(viewLifecycleOwner) {
            adapter.contacts = it
        }
    }

    override fun showContactInputDialog(contact: Contact) {
        val direction = ContactListFragmentDirections.actionContactListFragmentToContactInputDialogFragment(contact)
        findNavController().navigate(direction)
    }

    override fun getCustomAction(): Action {
        val onAction  = Runnable {
            val direction = ContactListFragmentDirections.actionContactListFragmentToContactInputDialogFragment(Contact())
            findNavController().navigate(direction)
        }
        fragmentResult().listenResult(Contact::class.java, viewLifecycleOwner) { contact ->
            viewModel.addContact(contact)
        }

        return Action(R.drawable.ic_add_contact_white, R.string.add_contact, onAction)
    }
}