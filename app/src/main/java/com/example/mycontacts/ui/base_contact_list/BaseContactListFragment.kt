package com.example.mycontacts.ui.base_contact_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mycontacts.R
import com.example.mycontacts.databinding.FragmentContactListBinding
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.domain.model.OnContactChangeListener
import com.example.mycontacts.ui.ui_utils.UiState
import com.example.mycontacts.ui.base_contact_list.adapter.ContactsAdapter
import com.example.mycontacts.ui.contract.fragmentResult
import com.example.mycontacts.ui.contract.sideEffects
import com.example.mycontacts.ui.tabs_screen.TabsFragmentDirections
import com.example.mycontacts.ui.ui_utils.RecyclerViewUtility
import com.example.mycontacts.ui.ui_utils.findTopLevelNavController
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

        binding.recyclerView.layoutManager = GridLayoutManager(
            requireContext(), RecyclerViewUtility.calculateNoOfColumns(
                requireContext(), Constants.CONTACTS_COLUMN_WIDTH
            )
        )
        val itemAnimator = binding.recyclerView.itemAnimator
        if (itemAnimator is DefaultItemAnimator) itemAnimator.supportsChangeAnimations = false

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
                showContactInputDialog(contact, CONTACT_CHANGE_REQ_KEY)
            }
        })
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contacts.collect { result ->
                    collectResult(result)
                }
            }
        }

        fragmentResult().listenResult(
            CONTACT_CHANGE_REQ_KEY, Contact::class.java, viewLifecycleOwner
        ) { newContact ->
            viewModel.updateContact(newContact)
        }
    }

    /*
        the heirs themselves will determine what message will be displayed if the list is empty
     */
    @StringRes
    abstract fun getEmptyListMessage(): Int

    /*
        use TabsFragmentDirections because BaseContactListFragment inheritors
        will be part of TabsFragment (will be placed in R.id.tabsFragmentContainer)
     */
    protected fun showContactInputDialog(contact: Contact, requestKey: String) {
        val direction = TabsFragmentDirections.actionTabsFragmentToContactInputDialogFragment(
            contact, requestKey
        )
        findTopLevelNavController().navigate(direction)
    }

    private fun collectResult(uiState: UiState<List<Contact>>) {
        hideSupportingViews()
        when (uiState) {
            is UiState.Loading -> binding.progressBar.visibility = View.VISIBLE
            is UiState.Success -> adapter.contacts = uiState.data
            is UiState.EmptyOrNull -> showEmptyListMessage(getString(getEmptyListMessage()))
            is UiState.Error -> showEmptyListMessage(
                uiState.message ?: getString(R.string.get_contact_list_failed)
            )
        }
    }

    private fun hideSupportingViews() {
        binding.progressBar.visibility = View.GONE
        binding.emptyListMessageGroup.visibility = View.GONE
    }

    private fun showEmptyListMessage(message: String) {
        // need to clear previous data (it updates automatically only if Result.isSuccess)
        adapter.contacts = emptyList()

        binding.emptyListMessageGroup.visibility = View.VISIBLE
        binding.emptyListMessage.text = message
    }

    companion object {
        private const val CONTACT_CHANGE_REQ_KEY = "CONTACT_CHANGE_REQ_KEY"
    }
}