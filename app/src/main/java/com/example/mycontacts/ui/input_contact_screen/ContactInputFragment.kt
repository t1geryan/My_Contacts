package com.example.mycontacts.ui.input_contact_screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mycontacts.R
import com.example.mycontacts.databinding.FragmentInputContactBinding
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.ui.contract.fragmentResult
import com.example.mycontacts.ui.contract.sideEffects
import com.example.mycontacts.ui.ui_utils.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactInputFragment : Fragment() {

    private lateinit var dialogBinding: FragmentInputContactBinding

    private val args: ContactInputFragmentArgs by navArgs()

    private val prevContact: Contact by lazy {
        args.prevContact
    }

    @Inject
    lateinit var factory: ContactInputViewModel.Factory

    private val viewModel: ContactInputViewModel by viewModelCreator {
        factory.create(prevContact)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // do nothing = block back button
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this, // LifecycleOwner
            callback
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        dialogBinding = FragmentInputContactBinding.inflate(inflater, container, false)

        dialogBinding.inputImageView.setOnClickListener {
            pickPhotoIfNotNull()
        }

        dialogBinding.confirmButton.setOnClickListener {
            setContactInputListener()
        }

        dialogBinding.cancelButton.setOnClickListener {
            back()
        }

        return dialogBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.photo.observe(viewLifecycleOwner) {
            Glide.with(requireContext()).load(it).centerCrop()
                .error(R.drawable.base_avatar_daynight).placeholder(R.drawable.base_avatar_daynight)
                .into(dialogBinding.inputImageView)
        }

        viewModel.name.observe(viewLifecycleOwner) {
            dialogBinding.inputNameEditText.setText(it)
        }

        viewModel.number.observe(viewLifecycleOwner) {
            dialogBinding.inputNumberEditText.setText(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.name.value = dialogBinding.inputNameEditText.text.toString()
        viewModel.number.value = dialogBinding.inputNumberEditText.text.toString()
    }

    private fun setContactInputListener() {

        val enteredTextName = dialogBinding.inputNameEditText.text.toString()
        val enteredTextNumber = dialogBinding.inputNumberEditText.text.toString()

        var isBadInput = false
        val emptyValueError = getString(R.string.empty_value)
        if (enteredTextName.isBlank()) {
            dialogBinding.inputNameEditText.error = emptyValueError
            isBadInput = true
        }
        if (enteredTextNumber.isBlank()) {
            dialogBinding.inputNumberEditText.error = emptyValueError
            isBadInput = true
        }

        if (isBadInput) return

        val newPhoto = (viewModel.photo.value ?: "").toString()
        val contact = prevContact.copy(
            photo = newPhoto, name = enteredTextName, number = enteredTextNumber
        )
        fragmentResult().publishResult(contact)
        back()
    }

    private fun pickPhotoIfNotNull() {
        sideEffects().pickPhoto {
            it?.let {
                viewModel.photo.value = it
            }
        }
    }
    private fun back() {
        findNavController().popBackStack()
    }
}