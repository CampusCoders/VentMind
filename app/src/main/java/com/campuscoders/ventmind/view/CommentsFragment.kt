package com.campuscoders.ventmind.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.campuscoders.ventmind.databinding.FragmentCommentsBinding
import com.campuscoders.ventmind.viewmodel.CommentsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentsFragment: Fragment() {

    private var _binding: FragmentCommentsBinding? = null
    private val binding get() = _binding!!

    val viewModel: CommentsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCommentsBinding.inflate(inflater,container,false)
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