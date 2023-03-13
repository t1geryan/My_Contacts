package com.example.mycontacts.ui.favorite_contacts_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.mycontacts.R
import com.example.mycontacts.ui.base.BaseContactListFragment
import com.example.mycontacts.ui.base.BaseContactListViewModel
import com.example.mycontacts.ui.details.HasCustomTitleToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteContactsListFragment : BaseContactListFragment(), HasCustomTitleToolbar {

    override val viewModel: BaseContactListViewModel by viewModels<FavoriteContactsListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (viewModel as FavoriteContactsListViewModel).contacts.observe(viewLifecycleOwner) {
            adapter.contacts = it
        }
    }

    override fun getTitle(): Int = R.string.favorite_contacts_title
}

