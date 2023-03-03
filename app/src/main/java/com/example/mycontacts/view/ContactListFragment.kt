package com.example.mycontacts.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mycontacts.App
import com.example.mycontacts.R
import com.example.mycontacts.databinding.FragmentContactListBinding
import com.example.mycontacts.model.Contact
import com.example.mycontacts.model.OnContactChangeListener
import com.example.mycontacts.model.ContactListLocalService
import com.example.mycontacts.model.ContactListRepository
import com.example.mycontacts.view.utils.*

class ContactListFragment : Fragment(), HasCustomActionToolbar, HasCustomTitleToolbar {

    private lateinit var binding: FragmentContactListBinding
    private lateinit var adapter: ContactsAdapter

    private lateinit var contactListLocalService: ContactListLocalService

    private lateinit var listener : ContactListRepository.OnContactListChangeListener
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

        contactListLocalService = (context?.applicationContext as App).contactListLocalService

        listener = contactListLocalService.OnContactListChangeListener {
            adapter.contacts = it
        }

        adapter = ContactsAdapter(object : OnContactChangeListener {
            override fun onDetails(contact: Contact) {
                InputContactDialogFragment.newInstance(contact.name, contact.number).show(parentFragmentManager, InputContactDialogFragment.TAG)
            }

            override fun onDeleteContact(contact: Contact) {
                contactListLocalService.deleteContact(contact)
            }

            override fun onChangeFavoriteStatus(contact: Contact) {
                contactListLocalService.changeContactFavoriteStatus(contact)
            }

        })

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(),
            RecyclerViewUtility.calculateNoOfColumns(requireContext(), 136F))
        binding.recyclerView.adapter = adapter
        adapter.contacts = contactListLocalService.contacts
        contactListLocalService.addListener(listener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        contactListLocalService.removeListener(listener)
    }

    override fun getCustomAction(): Action {
        val onAction  = Runnable {
            InputContactDialogFragment.newInstance().show(parentFragmentManager, InputContactDialogFragment.TAG)
        }
        InputContactDialogFragment.setupResultListener(parentFragmentManager, viewLifecycleOwner) { contact ->
            contactListLocalService.addContact(contact)
        }

        return Action(R.drawable.ic_add_contact_white, R.string.add_contact, onAction)
    }

    override fun getTitle(): Int {
        return R.string.contacts
    }
}