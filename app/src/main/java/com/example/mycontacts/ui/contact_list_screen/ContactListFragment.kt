package com.example.mycontacts.ui.contact_list_screen

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mycontacts.R
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.ui.base_contact_list.BaseContactListFragment
import com.example.mycontacts.ui.base_contact_list.BaseContactListViewModel
import com.example.mycontacts.ui.contract.Action
import com.example.mycontacts.ui.contract.HasCustomActionToolbar
import com.example.mycontacts.ui.contract.fragmentResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactListFragment : BaseContactListFragment(), HasCustomActionToolbar {

    override val viewModel: BaseContactListViewModel by viewModels<ContactListViewModel>()

    override fun showContactInputDialog(contact: Contact) {
        val direction =
            ContactListFragmentDirections.actionContactListFragmentToContactInputDialogFragment(
                contact
            )
        findNavController().navigate(direction)
    }

    override fun getCustomActionsList(): List<Action> {
        val onAction1 = Runnable {
            val direction =
                ContactListFragmentDirections.actionContactListFragmentToContactInputDialogFragment(
                    Contact()
                )
            findNavController().navigate(direction)
        }
        fragmentResult().listenResult(Contact::class.java, viewLifecycleOwner) { contact ->
            viewModel.addContact(contact)
        }

        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> (viewModel as ContactListViewModel).deleteAllContacts()
            }
        }
        val onAction2 = Runnable {
            AlertDialog.Builder(requireContext()).setTitle(R.string.confirm_dialog_title)
                .setMessage(R.string.confirm_dialog_clear_contacts_message)
                .setPositiveButton(R.string.confirm_dialog_positive, listener)
                .setNegativeButton(R.string.confirm_dialog_negative, listener).show()
        }

        return listOf(
            Action(R.drawable.ic_add_contact_white, R.string.add_contact, onAction1),
            Action(R.drawable.ic_clear_white, R.string.clear_all, onAction2)
        )
    }
}