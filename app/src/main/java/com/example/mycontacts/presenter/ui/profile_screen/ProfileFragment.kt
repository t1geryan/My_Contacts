package com.example.mycontacts.presenter.ui.profile_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.mycontacts.R
import com.example.mycontacts.databinding.FragmentProfileBinding
import com.example.mycontacts.presenter.contract.Action
import com.example.mycontacts.presenter.contract.HasCustomActionToolbar
import com.example.mycontacts.presenter.contract.sideEffects
import com.example.mycontacts.presenter.state.UiState
import com.example.mycontacts.presenter.ui_utils.collectWhenStarted
import dagger.hilt.android.AndroidEntryPoint

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
        binding.confirmButton.setOnClickListener {
            sideEffects().showToast(getString(R.string.data_saved))
            saveEnteredNameAndNumber()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectWhenStarted {
            viewModel.name.collect {
                collectUiState(it, "") { str ->
                    binding.profileNameET.setText(str)
                }
            }
        }
        collectWhenStarted {
            viewModel.number.collect {
                collectUiState(it, "") { str ->
                    binding.profileNumberET.setText(str)
                }
            }
        }
        collectWhenStarted {
            viewModel.photo.collect {
                collectUiState(it, "") { uriString ->
                    loadProfilePhoto(uriString)
                }
            }
        }
        collectWhenStarted {
            viewModel.contactsCount.collect {
                collectUiState(it, 0) { count ->
                    binding.contactsCount.text = getString(R.string.profile_contacts_count, count)
                }
            }
        }
        collectWhenStarted {
            viewModel.favContactsCount.collect {
                collectUiState(it, 0) { count ->
                    binding.favContactsCount.text =
                        getString(R.string.profile_fav_contacts_count, count)
                }
            }
        }
    }

    /*
        saving the entered data on destroy
        if they were not saved manually
     */
    override fun onDestroy() {
        super.onDestroy()
        saveEnteredNameAndNumber()
    }

    private fun <T> collectUiState(uiState: UiState<T>, emptyResult: T, showBlock: (T) -> Unit) {
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

    private fun saveEnteredNameAndNumber() {
        with(binding.profileNameET.text.toString()) {
            if (this != (viewModel.name.value as? UiState.Success)?.data)
                viewModel.setName(this)
        }
        with(binding.profileNumberET.text.toString()) {
            if (this != (viewModel.number.value as? UiState.Success)?.data)
                viewModel.setNumber(this)
        }
    }

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