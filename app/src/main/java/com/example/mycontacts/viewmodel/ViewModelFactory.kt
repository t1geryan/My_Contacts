package com.example.mycontacts.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.mycontacts.App

class ViewModelFactory(
    private val app: App,
) : AbstractSavedStateViewModelFactory() {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        val viewModel : ViewModel = when(modelClass) {
            ContactListViewModel::class.java -> ContactListViewModel(app.contactListLocalService, handle)
            else -> throw IllegalStateException("UnknownViewModelClassException")
        }

        @Suppress("UNCHECKED_CAST")
        return viewModel as T
    }
}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)
fun AppCompatActivity.factory() = ViewModelFactory(applicationContext as App)