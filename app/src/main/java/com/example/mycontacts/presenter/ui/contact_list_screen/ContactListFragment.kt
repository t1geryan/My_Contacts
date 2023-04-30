package com.example.mycontacts.presenter.ui.contact_list_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.mycontacts.R
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.presenter.contract.Action
import com.example.mycontacts.presenter.contract.HasCustomActionToolbar
import com.example.mycontacts.presenter.contract.fragmentResult
import com.example.mycontacts.presenter.contract.sideEffects
import com.example.mycontacts.presenter.ui.base_contact_list.BaseContactListFragment
import com.example.mycontacts.presenter.ui.base_contact_list.BaseContactListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactListFragment : BaseContactListFragment(), HasCustomActionToolbar {

    override val viewModel: BaseContactListViewModel by viewModels<ContactListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentResult().listenResult(
            CONTACT_ADD_REQUEST_KEY, Contact::class.java, viewLifecycleOwner
        ) { contact ->
            viewModel.addContact(contact)
        }
    }

    override fun getEmptyListMessage(): Int = R.string.no_contacts_added_message

    override fun getCustomActionsList(): List<Action> {
        val onAction1 = Runnable {
            showContactInputDialog(Contact(), CONTACT_ADD_REQUEST_KEY)
        }


        val onAction2 = Runnable {
            sideEffects().showConfirmDialog(
                R.string.confirm_dialog_clear_contacts_message, null
            ) { _, _ ->
                (viewModel as ContactListViewModel).deleteAllContacts()
            }
        }

        return listOf(
            Action(R.drawable.ic_add_contact_white, R.string.add_contact, onAction1),
            Action(R.drawable.ic_delete_daynight, R.string.clear_all, onAction2)
        )
    }

    companion object {
        const val CONTACT_ADD_REQUEST_KEY = "CONTACT_ADD_REQUEST_KEY"
    }
}