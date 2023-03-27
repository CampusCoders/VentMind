package com.campuscoders.ventmind.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.campuscoders.ventmind.R
import com.campuscoders.ventmind.databinding.FragmentExperienceBinding
import com.campuscoders.ventmind.util.UiState
import com.campuscoders.ventmind.util.hide
import com.campuscoders.ventmind.util.show
import com.campuscoders.ventmind.viewmodel.ExpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExperienceFragment: Fragment() {

    private var _binding: FragmentExperienceBinding? = null
    private val binding get() = _binding!!

    val viewModel: ExpViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentExperienceBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        viewModel.getPosts()

        binding.fabExperience.setOnClickListener {
            findNavController().navigate(R.id.action_experienceFragment_to_createPostFragment)
        }
    }

    private fun observer() {
        viewModel.posts.observe(viewLifecycleOwner) {state ->
            when(state) {
                is UiState.Loading -> {
                    binding.progressBarExp.show()
                }
                is UiState.Success -> {
                    binding.progressBarExp.hide()
                }
                is UiState.Failure -> {
                    binding.progressBarExp.hide()
                }
            }
        }
        // update like count
        // update dislike count
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}