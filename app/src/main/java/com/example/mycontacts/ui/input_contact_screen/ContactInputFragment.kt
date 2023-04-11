package com.example.mycontacts.ui.input_contact_screen

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mycontacts.R
import com.example.mycontacts.databinding.FragmentInputContactBinding
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.ui.contract.fragmentResult
import com.example.mycontacts.ui.contract.sideEffects
import com.example.mycontacts.ui.ui_utils.findTopLevelNavController
import com.example.mycontacts.ui.ui_utils.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactInputFragment : Fragment() {

    private lateinit var binding: FragmentInputContactBinding

    private val args: ContactInputFragmentArgs by navArgs()

    private val prevContact: Contact by lazy {
        args.prevContact
    }

    private val requestKey: String by lazy {
        args.requestKey
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
        binding = FragmentInputContactBinding.inflate(inflater, container, false)

        binding.inputImageLayout.inputImageView.setOnClickListener {
            pickPhotoIfNotNull()
        }

        binding.confirmButton.setOnClickListener {
            setContactInputListener()
        }

        binding.cancelButton.setOnClickListener {
            back()
        }

        binding.inputImageLayout.removeImageButton.setOnClickListener {
            removePhoto()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.photo.observe(viewLifecycleOwner) { uri ->
            loadPhoto(uri)
        }

        viewModel.name.observe(viewLifecycleOwner) {
            binding.inputNameEditText.setText(it)
        }

        viewModel.number.observe(viewLifecycleOwner) {
            binding.inputNumberEditText.setText(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.name.value = binding.inputNameEditText.text.toString()
        viewModel.number.value = binding.inputNumberEditText.text.toString()
    }

    private fun setContactInputListener() {
        val enteredTextName = binding.inputNameEditText.text.toString()
        val enteredTextNumber = binding.inputNumberEditText.text.toString()

        var isBadInput = false
        val emptyValueError = getString(R.string.empty_value)
        if (enteredTextName.isBlank()) {
            binding.inputNameEditText.error = emptyValueError
            isBadInput = true
        }
        if (enteredTextNumber.isBlank()) {
            binding.inputNumberEditText.error = emptyValueError
            isBadInput = true
        }

        if (isBadInput) return

        val newPhoto = (viewModel.photo.value ?: "").toString()
        val contact = prevContact.copy(
            photo = newPhoto, name = enteredTextName, number = enteredTextNumber
        )
        fragmentResult().publishResult(requestKey, contact)
        back()
    }

    private fun pickPhotoIfNotNull() {
        sideEffects().pickPhoto {
            it?.let {
                viewModel.photo.value = it
            }
        }
    }

    private fun loadPhoto(uri: Uri?) {
        binding.inputImageLayout.removeImageButton.visibility = if (uri != null) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
        Glide.with(requireContext()).load(uri).centerCrop().error(R.drawable.ic_camera_daynight)
            .placeholder(R.drawable.ic_camera_daynight).into(binding.inputImageLayout.inputImageView)
    }
    private fun removePhoto() {
        viewModel.photo.value = null
    }


    private fun back() {
        findTopLevelNavController().popBackStack(R.id.tabsFragment, false)
    }
}