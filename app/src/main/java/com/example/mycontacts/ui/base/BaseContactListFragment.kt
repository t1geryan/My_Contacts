package com.example.mycontacts.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mycontacts.databinding.FragmentContactListBinding
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.domain.model.OnContactChangeListener
import com.example.mycontacts.ui.contact_list_utils.ContactsAdapter
import com.example.mycontacts.ui.details.RecyclerViewUtility
import com.example.mycontacts.ui.navigation.navigator
import com.example.mycontacts.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseContactListFragment protected constructor() : Fragment() {

    protected lateinit var binding: FragmentContactListBinding

    /*
        Can't be injected
        Because nobody implements the onContactChangeListener interface (only anonymous object)
     */
    protected lateinit var adapter: ContactsAdapter

    protected abstract val viewModel : BaseContactListViewModel

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
            override fun onDelete(contact: Contact) {
                viewModel.deleteContact(contact)
            }

            override fun onChangeFavoriteStatus(contact: Contact) {
                viewModel.changeContactFavoriteStatus(contact)
            }

            override fun onCall(contact: Contact) {
                navigator().showToast("Coming Soon")
            }

            override fun onChangeData(contact: Contact) {
                navigator().launchContactInputScreen(contact.name, contact.number)
            }
        })

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(),
            RecyclerViewUtility.calculateNoOfColumns(requireContext(), Constants.CONTACTS_COLUMN_WIDTH))
        val itemAnimator = binding.recyclerView.itemAnimator
        if (itemAnimator is DefaultItemAnimator)
            itemAnimator.supportsChangeAnimations = false

        binding.recyclerView.adapter = adapter
    }
}