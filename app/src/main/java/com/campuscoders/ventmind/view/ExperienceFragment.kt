package com.campuscoders.ventmind.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.campuscoders.ventmind.R
import com.campuscoders.ventmind.adapter.ExpAdapter
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

    private val expAdapter by lazy {
        ExpAdapter(
            avatarOnItemClickListener = {
                findNavController().navigate(R.id.action_experienceFragment_to_profileFragment, Bundle().apply {
                    putString("user_id",it)
                })
            },
            usernameOnItemClickListener = {
                findNavController().navigate(R.id.action_experienceFragment_to_profileFragment, Bundle().apply {
                    putString("user_id",it)
                })
            },
            likeOnItemClickListener = {
                viewModel.updateLike(it)
            },
            dislikeOnItemClickListener = {
                viewModel.updateDislike(it)
            }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentExperienceBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recyclerView
        binding.recyclerViewExperience.adapter = expAdapter
        binding.recyclerViewExperience.layoutManager = LinearLayoutManager(requireContext())

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
                    expAdapter.updateList(state.data.toMutableList())
                }
                is UiState.Failure -> {
                    binding.progressBarExp.hide()
                }
            }
        }
        viewModel.updateLikeCount.observe(viewLifecycleOwner) {state ->
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
        viewModel.updateDislikeCount.observe(viewLifecycleOwner) {state ->
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}