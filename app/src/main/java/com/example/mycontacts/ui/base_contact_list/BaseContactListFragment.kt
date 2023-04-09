package com.example.mycontacts.ui.base_contact_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mycontacts.databinding.FragmentContactListBinding
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.domain.model.OnContactChangeListener
import com.example.mycontacts.ui.base_contact_list.adapter.ContactsAdapter
import com.example.mycontacts.ui.contract.fragmentResult
import com.example.mycontacts.ui.contract.sideEffects
import com.example.mycontacts.ui.findTopLevelNavController
import com.example.mycontacts.ui.tabs_screen.TabsFragmentDirections
import com.example.mycontacts.ui.ui_utils.RecyclerViewUtility
import com.example.mycontacts.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
abstract class BaseContactListFragment protected constructor() : Fragment() {

    private lateinit var binding: FragmentContactListBinding

    /*
        Can't be injected
        Because nobody implements the onContactChangeListener interface (only anonymous object)
     */
    protected open lateinit var adapter: ContactsAdapter

    abstract val viewModel: BaseContactListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentContactListBinding.inflate(inflater, container, false)


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contacts.collect {
                    adapter.contacts = it
                }
            }
        }

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
                sideEffects().startCall(contact)
            }

            override fun onChangeData(contact: Contact) {
                showContactInputDialog(contact)
                fragmentResult().listenResult(
                    Contact::class.java, viewLifecycleOwner
                ) { newContact ->
                    viewModel.updateContact(newContact)
                }
            }
        })

        binding.recyclerView.layoutManager = GridLayoutManager(
            requireContext(), RecyclerViewUtility.calculateNoOfColumns(
                requireContext(), Constants.CONTACTS_COLUMN_WIDTH
            )
        )
        val itemAnimator = binding.recyclerView.itemAnimator
        if (itemAnimator is DefaultItemAnimator) itemAnimator.supportsChangeAnimations = false

        binding.recyclerView.adapter = adapter
    }

    /*
        use TabsFragmentDirections because BaseContactListFragment inheritors
        will be part of TabsFragment (will be placed in R.id.tabsFragmentContainer)
     */
    protected fun showContactInputDialog(contact: Contact) {
        val direction = TabsFragmentDirections.actionTabsFragmentToContactInputDialogFragment(
            contact
        )
        findTopLevelNavController().navigate(direction)
    }
}