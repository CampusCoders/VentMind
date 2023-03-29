package com.campuscoders.ventmind.view

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.campuscoders.ventmind.MainActivity
import com.campuscoders.ventmind.R
import com.campuscoders.ventmind.adapter.FeedAdapter
import com.campuscoders.ventmind.databinding.FragmentProfileBinding
import com.campuscoders.ventmind.util.*
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

    private var userIdFromPost: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? MainActivity)?.setSupportActionBar(binding.toolbarProfile)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.profile_menu,menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
                return true
            }

        },viewLifecycleOwner)

        val userId = arguments?.getString("user_id",null)
        userId?.let {
            viewModel.getUser(it)
            viewModel.getPosts(it)
            userIdFromPost = it
        }

        if(userIdFromPost == "") {
            // own profile
            binding.imageViewProfileAvatar.setOnClickListener {
                // avatar fragment
                findNavController().navigate(R.id.action_profileFragment_to_avatarsFragment)
            }
        }

        // recyclerView
        binding.recyclerViewProfile.adapter = feedAdapter
        binding.recyclerViewProfile.layoutManager = LinearLayoutManager(requireContext())

        viewModel.checkUser(userIdFromPost)

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
                binding.imageViewProfileEditIcon.show()
                binding.imageViewProfileUpdateIcon.hide()
            }
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
                    binding.imageViewProfileAvatar.downloadFromUrl(state.data.user_avatar, placeHolderProgressBar(requireContext()))
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
                // true ise kullanıcının profil ekranıdır. edit ikonu gösterilecek, update saklanacak
                binding.imageViewProfileEditIcon.show()
                binding.imageViewProfileUpdateIcon.hide()
            } else {
                // başka birinin profil ekranı ise edit ve update iconları saklanacak
                binding.imageViewProfileEditIcon.hide()
                binding.imageViewProfileUpdateIcon.hide()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}