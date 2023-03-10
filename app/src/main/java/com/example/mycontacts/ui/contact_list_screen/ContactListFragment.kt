package com.example.mycontacts.ui.contact_list_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mycontacts.R
import com.example.mycontacts.databinding.FragmentContactListBinding
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.domain.model.OnContactChangeListener
import com.example.mycontacts.ui.contact_list_screen.utils.ContactsAdapter
import com.example.mycontacts.ui.details.Action
import com.example.mycontacts.ui.details.HasCustomActionToolbar
import com.example.mycontacts.ui.details.HasCustomTitleToolbar
import com.example.mycontacts.ui.details.RecyclerViewUtility
import com.example.mycontacts.ui.input_contact_screen.InputContactDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactListFragment : Fragment(), HasCustomActionToolbar, HasCustomTitleToolbar {

    private lateinit var binding: FragmentContactListBinding

    /*
        Can't be injected
        Because nobody implements the onContactChangeListener interface (only anonymous object)
     */
    private lateinit var adapter: ContactsAdapter

    private val viewModel by viewModels<ContactListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentContactListBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ContactsAdapter(object : OnContactChangeListener {
            override fun onDeleteContact(contact: Contact) {
                viewModel.deleteContact(contact)
            }

            override fun onChangeFavoriteStatus(contact: Contact) {
                viewModel.changeContactFavoriteStatus(contact)
            }
        })

        viewModel.contacts.observe(viewLifecycleOwner) {
            adapter.contacts = it
        }

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(),
            RecyclerViewUtility.calculateNoOfColumns(requireContext(), 136F))
        binding.recyclerView.adapter = adapter
    }

    override fun getCustomAction(): Action {
        val onAction  = Runnable {
            InputContactDialogFragment.newInstance()
                .show(parentFragmentManager, InputContactDialogFragment.TAG)
        }
        InputContactDialogFragment.setupResultListener(
            parentFragmentManager,
            viewLifecycleOwner
        ) { contact ->
            viewModel.addContact(contact)
        }

        return Action(R.drawable.ic_add_contact_white, R.string.add_contact, onAction)
    }

    override fun getTitle(): Int = R.string.contact_list_title
}