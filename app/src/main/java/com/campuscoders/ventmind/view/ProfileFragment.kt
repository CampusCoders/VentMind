package com.campuscoders.ventmind.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.campuscoders.ventmind.adapter.FeedAdapter
import com.campuscoders.ventmind.databinding.FragmentProfileBinding
import com.campuscoders.ventmind.util.UiState
import com.campuscoders.ventmind.util.hide
import com.campuscoders.ventmind.util.show
import com.campuscoders.ventmind.util.toast
import com.campuscoders.ventmind.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment: Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    val viewModel: ProfileViewModel by viewModels()
    val feedAdapter by lazy {
        FeedAdapter(
            avatarOnItemClickListener = {},
            usernameOnItemClickListener = {},
            likeOnItemClickListener = {
                toast(it)
            },
            commentOnItemClickListener = {
                toast(it)
            }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recyclerView
        binding.recyclerViewProfile.adapter = feedAdapter
        binding.recyclerViewProfile.layoutManager = LinearLayoutManager(requireContext())

        viewModel.checkUser()

        // ui
        binding.editTextProfileBio.isEnabled = false

        binding.imageViewProfileEditIcon.setOnClickListener {
            binding.editTextProfileBio.isEnabled = true
            binding.imageViewProfileEditIcon.hide()
            binding.imageViewProfileUpdateIcon.show()
        }

        binding.imageViewProfileUpdateIcon.setOnClickListener {
            if(binding.editTextProfileBio.text.isNullOrEmpty()) {
                toast("Enter bio")
            } else {
                viewModel.setBio(binding.editTextProfileBio.text.toString())
            }
        }

        val userId = arguments?.getString("user_id",null)
        userId?.let {
            viewModel.getUser(it)
            viewModel.getPosts(it)
        }

        observer()
    }

    private fun observer() {
        viewModel.user.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UiState.Loading -> {
                    binding.progressBarProfile.show()
                }
                is UiState.Success -> {
                    binding.progressBarProfile.hide()
                    binding.textViewProfileUserName.text = state.data.user_nick
                    binding.textViewProfileScore.text = state.data.user_score.toString()
                    binding.editTextProfileBio.setText(state.data.user_bio)
                    // avatar
                }
                is UiState.Failure -> {
                    binding.progressBarProfile.hide()
                    toast(state.error!!)
                }
            }
        }
        viewModel.post.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UiState.Loading -> {
                    binding.progressBarProfile.show()
                }
                is UiState.Success -> {
                    binding.progressBarProfile.hide()
                    // recycler view
                    feedAdapter.updateList(state.data.toMutableList())
                }
                is UiState.Failure -> {
                    binding.progressBarProfile.hide()
                    toast(state.error!!)
                }
            }
        }
        viewModel.bio.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UiState.Loading -> {
                    binding.progressBarProfile.show()
                }
                is UiState.Success -> {
                    binding.progressBarProfile.hide()
                    binding.editTextProfileBio.isEnabled = false
                    binding.imageViewProfileUpdateIcon.hide()
                    binding.imageViewProfileEditIcon.show()
                }
                is UiState.Failure -> {
                    binding.progressBarProfile.hide()
                    toast(state.error!!)
                }
            }
        }
        viewModel.checkUser.observe(viewLifecycleOwner) { state ->
            if (state) {
                // true ise kullanıcının profil ekranıdır
                binding.imageViewProfileEditIcon.show()
                binding.imageViewProfileUpdateIcon.hide()
            } else {
                // başka birinin profil ekranı
                binding.linearEdit.hide()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}