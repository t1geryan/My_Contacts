package com.example.mycontacts.ui.contact_list_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.mycontacts.R
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.ui.base.BaseContactListFragment
import com.example.mycontacts.ui.base.BaseContactListViewModel
import com.example.mycontacts.ui.details.Action
import com.example.mycontacts.ui.details.HasCustomActionToolbar
import com.example.mycontacts.ui.details.HasCustomTitleToolbar
import com.example.mycontacts.ui.navigation.navigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactListFragment : BaseContactListFragment(), HasCustomActionToolbar, HasCustomTitleToolbar {

    override val viewModel: BaseContactListViewModel by viewModels<ContactListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (viewModel as ContactListViewModel).contacts.observe(viewLifecycleOwner) {
            adapter.contacts = it
        }
    }

    override fun getCustomAction(): Action {
        val onAction  = Runnable {
            navigator().launchContactInputScreen()
        }
        navigator().listenResult(Contact::class.java, viewLifecycleOwner) { contact ->
            viewModel.addContact(contact)
        }

        return Action(R.drawable.ic_add_contact_white, R.string.add_contact, onAction)
    }

    override fun getTitle(): Int = R.string.contact_list_title
}