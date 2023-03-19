package com.campuscoders.ventmind.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.campuscoders.ventmind.databinding.FragmentAvatarsBinding
import com.campuscoders.ventmind.viewmodel.AvatarsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AvatarsFragment: Fragment() {

    private var _binding: FragmentAvatarsBinding? = null
    private val binding get() = _binding!!

    val viewModel: AvatarsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAvatarsBinding.inflate(inflater,container,false)
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