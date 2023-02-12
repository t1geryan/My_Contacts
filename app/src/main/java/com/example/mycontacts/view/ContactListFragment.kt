package com.example.mycontacts.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mycontacts.R
import com.example.mycontacts.databinding.FragmentContactListBinding
import com.example.mycontacts.model.Contact
import com.example.mycontacts.model.Repository
import com.example.mycontacts.view.utils.*

class ContactListFragment : Fragment(), HasCustomActionToolbar, HasCustomTitleToolbar {

    private lateinit var binding: FragmentContactListBinding
    private lateinit var adapter: ContactsAdapter
    private val listener = Repository.ContactListChangeListener {
        adapter.contacts = it
    }
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
        adapter = ContactsAdapter()
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(),
            Utility.calculateNoOfColumns(requireContext(), 136F))
        binding.recyclerView.adapter = adapter
        adapter.contacts = Repository.contacts
        Repository.addListener(listener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Repository.removeListener(listener)
    }
    override fun getCustomAction(): Action {
        return Action(R.drawable.ic_add_contact_white, R.string.add_contact) {
            Repository.addContact(Contact("Test", "Test"))
        }
    }

    override fun getTitle(): Int {
        return R.string.contacts
    }
}