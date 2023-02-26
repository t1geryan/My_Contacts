package com.example.mycontacts.view

import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import com.example.mycontacts.databinding.ItemInputContactBinding
import com.example.mycontacts.model.Contact


class InputContactDialogFragment : DialogFragment() {

    private lateinit var dialogBinding: ItemInputContactBinding

    private lateinit var prevName: String
    private lateinit var prevNumber: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = ItemInputContactBinding.inflate(layoutInflater)

        prevName = savedInstanceState?.getString(ARG_NAME) ?: requireArguments().getString(ARG_NAME, "")
        prevNumber = savedInstanceState?.getString(ARG_NUMBER) ?: requireArguments().getString(ARG_NUMBER, "")

        return createDialog()
    }

    private fun createDialog() : AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Input Contact")
            .setView(dialogBinding.root)
            .setPositiveButton("Confirm", null)
            .setNeutralButton("Cancel", null)
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

                var isRightInput = true
                if (enteredTextName.isBlank()) {
                    dialogBinding.inputNameEditText.error = "Value is empty"
                    isRightInput = false
                }
                if (enteredTextNumber.isBlank()) {
                    dialogBinding.inputNumberEditText.error = "Value is empty"
                    isRightInput = false
                }

                if (!isRightInput)
                    return@setOnClickListener
                Log.d("Test", "Right Input")
                val contact = Contact(enteredTextName, enteredTextNumber)
                parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf( Pair(RESPONSE_KEY,  contact)))
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
        val TAG: String = InputContactDialogFragment::class.java.simpleName
        @JvmStatic
        val REQUEST_KEY = "$TAG:defaultRequestKey"
        @JvmStatic
        val RESPONSE_KEY = "$TAG:RESPONSE"
        @JvmStatic
        private val ARG_NAME = "ARG_NAME"
        @JvmStatic
        private val ARG_NUMBER = "ARG_NUMBER"

        @JvmStatic
        fun newInstance(previousName: String = "", previousNumber: String = "") : InputContactDialogFragment {
            val args = bundleOf(ARG_NAME to previousName, ARG_NUMBER to previousNumber)
            val fragment = InputContactDialogFragment()
            fragment.arguments = args
            return fragment
        }

        @JvmStatic
        fun setupResultListener(manager: FragmentManager, lifecycleOwner: LifecycleOwner, listener: (Contact) -> Unit) {
            val fragmentResultListener = FragmentResultListener { _, result ->
                val contact: Contact =
                    if (Build.VERSION.SDK_INT >= 33)
                        result.getParcelable(RESPONSE_KEY, Contact::class.java) ?: throw Exception("NoFragmentResultException")
                    else
                        @Suppress("DEPRECATION")
                        result.getParcelable(RESPONSE_KEY) ?: throw Exception("NoFragmentResultException")
                listener.invoke(contact)
            }

            manager.setFragmentResultListener(REQUEST_KEY, lifecycleOwner, fragmentResultListener)
        }
    }

}