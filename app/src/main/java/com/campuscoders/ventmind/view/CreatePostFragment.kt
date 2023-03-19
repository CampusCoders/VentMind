package com.campuscoders.ventmind.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.campuscoders.ventmind.databinding.FragmentCreatePostBinding
import com.campuscoders.ventmind.viewmodel.CreatePostViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePostFragment: Fragment() {

    private var _binding: FragmentCreatePostBinding? = null
    private val binding get() = _binding!!

    val viewModel: CreatePostViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCreatePostBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //logic
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}