package com.campuscoders.ventmind.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.campuscoders.ventmind.databinding.FragmentUserListBinding
import com.campuscoders.ventmind.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListFragment: Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    val viewModel: ListViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentUserListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // logic
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}