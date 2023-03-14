package com.example.mycontacts.ui.input_contact_screen

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.mycontacts.R
import com.example.mycontacts.databinding.ItemInputContactBinding
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.ui.navigation.navigator


class ContactInputDialogFragment : DialogFragment() {

    private lateinit var dialogBinding: ItemInputContactBinding

    private lateinit var prevName: String
    private lateinit var prevNumber: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = ItemInputContactBinding.inflate(layoutInflater)

        prevName = savedInstanceState?.getString(ARG_NAME) ?: requireArguments().getString(ARG_NAME, "")
        prevNumber = savedInstanceState?.getString(ARG_NUMBER) ?: requireArguments().getString(
            ARG_NUMBER, "")

        return createDialog()
    }

    private fun createDialog() : AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.input_contact)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.confirm, null)
            .setNeutralButton(R.string.cancel, null)
            .create()
            .apply {
                setCanceledOnTouchOutside(false)
                setContactInputListener(this)
            }
    }

    private fun setContactInputListener(dialog: AlertDialog) {
        dialog.setOnShowListener {
            dialogBinding.inputNameEditText.setText(prevName)
            dialogBinding.inputNumberEditText.setText(prevNumber)

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
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

                if (isBadInput)
                    return@setOnClickListener

                Log.d("Test", "Right Input")
                val contact = Contact(enteredTextName, enteredTextNumber)
                navigator().publishResult(contact)
                dismiss()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ARG_NAME, dialogBinding.inputNameEditText.text.toString())
        outState.putString(ARG_NUMBER, dialogBinding.inputNumberEditText.text.toString())
    }

    companion object {
        @JvmStatic
        val TAG: String = ContactInputDialogFragment::class.java.simpleName
        @JvmStatic
        private val ARG_NAME = "ARG_NAME"
        @JvmStatic
        private val ARG_NUMBER = "ARG_NUMBER"

        @JvmStatic
        fun newInstance(previousName: String = "", previousNumber: String = "") : ContactInputDialogFragment {
            val args = bundleOf(ARG_NAME to previousName, ARG_NUMBER to previousNumber)
            val fragment = ContactInputDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

}