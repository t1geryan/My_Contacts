package com.example.mycontacts.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mycontacts.R
import com.example.mycontacts.databinding.FragmentContactListBinding
import com.example.mycontacts.model.Contact
import com.example.mycontacts.model.OnContactChangeListener
import com.example.mycontacts.view.utils.*
import com.example.mycontacts.viewmodel.ContactListViewModel
import com.example.mycontacts.viewmodel.factory

class ContactListFragment : Fragment(), HasCustomActionToolbar, HasCustomTitleToolbar {

    private lateinit var binding: FragmentContactListBinding
    private lateinit var adapter: ContactsAdapter

    private val viewModel by viewModels<ContactListViewModel> { factory() }

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
            override fun onDetails(contact: Contact) {
                InputContactDialogFragment.newInstance(contact.name, contact.number).show(parentFragmentManager, InputContactDialogFragment.TAG)
            }

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
            InputContactDialogFragment.newInstance().show(parentFragmentManager, InputContactDialogFragment.TAG)
        }
        InputContactDialogFragment.setupResultListener(parentFragmentManager, viewLifecycleOwner) { contact ->
            viewModel.addContact(contact)
        }

        return Action(R.drawable.ic_add_contact_white, R.string.add_contact, onAction)
    }

    override fun getTitle(): Int {
        return R.string.contacts
    }
}