package com.example.mycontacts.presenter.ui_utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.mycontacts.R
import kotlinx.coroutines.launch

fun Fragment.findTopLevelNavController(): NavController {
    val topLevelHost =
        requireActivity().supportFragmentManager.findFragmentById(R.id.rootFragmentContainer) as NavHostFragment?
    return topLevelHost?.navController ?: findNavController()
}

fun Fragment.collectWhenStarted(collectBlock: suspend () -> Unit) =
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            collectBlock()
        }
    }