package com.example.mycontacts.ui.favorite_contacts_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.mycontacts.R
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.domain.model.OnContactChangeListener
import com.example.mycontacts.ui.base.BaseContactListFragment
import com.example.mycontacts.ui.contact_list_utils.ContactsAdapter
import com.example.mycontacts.ui.details.HasCustomTitleToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteContactsListFragment : BaseContactListFragment(), HasCustomTitleToolbar {
    /*
        Can't be injected
        Because nobody implements the onContactChangeListener interface (only anonymous object)
     */
    private lateinit var adapter: ContactsAdapter

    private val viewModel by viewModels<FavoriteContactsListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ContactsAdapter(object : OnContactChangeListener {
            override fun onChangeFavoriteStatus(contact: Contact) {
                viewModel.changeContactFavoriteStatus(contact)
            }
        })

        viewModel.contacts.observe(viewLifecycleOwner) {
            adapter.contacts = it
        }

        binding.recyclerView.adapter = adapter
    }

    override fun getTitle(): Int = R.string.favorite_contacts_title
}

