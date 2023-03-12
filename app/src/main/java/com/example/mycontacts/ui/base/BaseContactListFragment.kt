package com.example.mycontacts.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mycontacts.databinding.FragmentContactListBinding
import com.example.mycontacts.ui.details.RecyclerViewUtility
import com.example.mycontacts.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseContactListFragment protected constructor() : Fragment() {

    protected lateinit var binding: FragmentContactListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentContactListBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(),
            RecyclerViewUtility.calculateNoOfColumns(requireContext(), Constants.CONTACTS_COLUMN_WIDTH))
    }
}