package com.example.mycontacts.ui.profile_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.mycontacts.R
import com.example.mycontacts.databinding.FragmentProfileBinding
import com.example.mycontacts.ui.ui_utils.UiState
import com.example.mycontacts.ui.contract.Action
import com.example.mycontacts.ui.contract.HasCustomActionToolbar
import com.example.mycontacts.ui.contract.sideEffects
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment(), HasCustomActionToolbar {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.profileImageIV.setOnClickListener {
            sideEffects().pickPhoto { uri ->
                viewModel.setPhotoUri(uri.toString())
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectWhenStarted {
            viewModel.name.collect {
                collectResult(it, getString(R.string.empty_name)) { str ->
                    binding.profileNameTV.text = str
                }
            }
        }
        collectWhenStarted {
            viewModel.number.collect {
                collectResult(it, getString(R.string.empty_number)) { str ->
                    binding.profileNumberTV.text = str
                }
            }
        }
        collectWhenStarted {
            viewModel.photo.collect {
                collectResult(it, "") { uriString ->
                    loadProfilePhoto(uriString)
                }
            }
        }
        collectWhenStarted {
            viewModel.contactsCount.collect {
                collectResult(it, 0) { count ->
                    binding.contactsCount.text = getString(R.string.profile_contacts_count, count)
                }
            }
        }
        collectWhenStarted {
            viewModel.favContactsCount.collect {
                collectResult(it, 0) { count ->
                    binding.favContactsCount.text =
                        getString(R.string.profile_fav_contacts_count, count)
                }
            }
        }
    }

    private fun collectWhenStarted(collectBlock: suspend () -> Unit) =
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectBlock()
            }
        }

    private fun <T> collectResult(uiState: UiState<T>, emptyResult: T, showBlock: (T) -> Unit) {
        binding.progressBar.visibility = View.GONE
        when (uiState) {
            is UiState.EmptyOrNull -> showBlock(emptyResult)
            is UiState.Error -> sideEffects().showToast(getString(R.string.fail))
            is UiState.Loading -> binding.progressBar.visibility = View.VISIBLE
            is UiState.Success -> showBlock(uiState.data)
        }
    }

    private fun loadProfilePhoto(uriString: String) = Glide.with(this)
        .load(uriString)
        .centerCrop()
        .placeholder(R.drawable.base_avatar_daynight)
        .error(R.drawable.base_avatar_daynight)
        .into(binding.profileImageIV)

    override fun getCustomActionsList(): List<Action> {
        val onAction2 = Runnable {
            sideEffects().syncContacts {
                sideEffects().showConfirmDialog(
                    R.string.confirm_dialog_sync_message, null
                ) { _, _ ->
                    viewModel.syncContacts()
                }
            }
        }

        return listOf(
            Action(R.drawable.ic_sync_white, R.string.sync_contacts, onAction2),
        )
    }
}