package com.example.mycontacts.ui.favorite_contacts_screen

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
import com.example.mycontacts.ui.contact_list_screen.ContactListViewModel
import com.example.mycontacts.ui.contact_list_utils.ContactsAdapter
import com.example.mycontacts.ui.details.HasCustomTitleToolbar
import com.example.mycontacts.ui.details.RecyclerViewUtility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteContactsListFragment : Fragment(), HasCustomTitleToolbar {

    private lateinit var binding: FragmentContactListBinding

    /*
        Can't be injected
        Because nobody implements the onContactChangeListener interface (only anonymous object)
     */
    private lateinit var adapter: ContactsAdapter

    private val viewModel by viewModels<FavoriteContactsListViewModel>()

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

    override fun getTitle(): Int = R.string.favorite_contacts_title
}

