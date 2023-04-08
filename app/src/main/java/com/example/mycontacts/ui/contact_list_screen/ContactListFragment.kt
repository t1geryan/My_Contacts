package com.example.mycontacts.ui.contact_list_screen

import androidx.fragment.app.viewModels
import com.example.mycontacts.R
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.ui.base_contact_list.BaseContactListFragment
import com.example.mycontacts.ui.base_contact_list.BaseContactListViewModel
import com.example.mycontacts.ui.contract.Action
import com.example.mycontacts.ui.contract.HasCustomActionToolbar
import com.example.mycontacts.ui.contract.fragmentResult
import com.example.mycontacts.ui.contract.sideEffects
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactListFragment : BaseContactListFragment(), HasCustomActionToolbar {

    override val viewModel: BaseContactListViewModel by viewModels<ContactListViewModel>()

    override fun getCustomActionsList(): List<Action> {
        val onAction1 = Runnable {
            showContactInputDialog(Contact())
        }
        fragmentResult().listenResult(Contact::class.java, viewLifecycleOwner) { contact ->
            viewModel.addContact(contact)
        }

        val onAction2 = Runnable {
            sideEffects().showConfirmDialog(
                R.string.confirm_dialog_sync_message, null
            ) { _, _ ->
                sideEffects().syncContacts {
                    (viewModel as ContactListViewModel).syncContacts()
                }
            }

        }
        val onAction3 = Runnable {
            sideEffects().showConfirmDialog(
                R.string.confirm_dialog_clear_contacts_message, null
            ) { _, _ ->
                (viewModel as ContactListViewModel).deleteAllContacts()
            }
        }

        return listOf(
            Action(R.drawable.ic_add_contact_white, R.string.add_contact, onAction1),
            Action(R.drawable.ic_sync_white, R.string.sync_contacts, onAction2),
            Action(R.drawable.ic_clear_white, R.string.clear_all, onAction3)
        )
    }
}