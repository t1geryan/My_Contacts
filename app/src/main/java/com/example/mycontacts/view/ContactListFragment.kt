package com.example.mycontacts.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mycontacts.databinding.FragmentContactListBinding
import com.example.mycontacts.model.Repository
import com.example.mycontacts.view.adapter.ContactsAdapter

class ContactListFragment : Fragment() {

    private lateinit var binding: FragmentContactListBinding
    private lateinit var adapter: ContactsAdapter

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
    }
}