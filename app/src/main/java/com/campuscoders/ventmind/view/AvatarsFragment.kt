package com.campuscoders.ventmind.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.campuscoders.ventmind.adapter.AvatarAdapter
import com.campuscoders.ventmind.databinding.FragmentAvatarsBinding
import com.campuscoders.ventmind.viewmodel.AvatarsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AvatarsFragment: Fragment() {

    private var _binding: FragmentAvatarsBinding? = null
    private val binding get() = _binding!!

    val viewModel: AvatarsViewModel by viewModels()

    private val avatarAdaper by lazy{
        AvatarAdapter()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAvatarsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewAvatars.adapter = avatarAdaper
        binding.recyclerViewAvatars.layoutManager = LinearLayoutManager(requireContext())

        observer()
    }

    private fun observer(){

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}