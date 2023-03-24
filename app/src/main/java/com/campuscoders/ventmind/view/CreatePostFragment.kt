package com.campuscoders.ventmind.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.campuscoders.ventmind.databinding.FragmentCreatePostBinding
import com.campuscoders.ventmind.model.PostExp
import com.campuscoders.ventmind.model.PostFeed
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.util.UiState
import com.campuscoders.ventmind.util.hide
import com.campuscoders.ventmind.util.show
import com.campuscoders.ventmind.util.toast
import com.campuscoders.ventmind.viewmodel.CreatePostViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class CreatePostFragment: Fragment() {

    private var _binding: FragmentCreatePostBinding? = null
    private val binding get() = _binding!!

    val viewModel: CreatePostViewModel by viewModels()

    private var userObj: User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCreatePostBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUser()

        observer()

        binding.buttonShareFeeling.setOnClickListener {
            if(validation()) {
                viewModel.addPostFeed(createPostFeed())
            }
        }
        binding.buttonShareExperience.setOnClickListener {
            if(validation()) {
                viewModel.addPostExp(createPostExp())
            }
        }
    }

    private fun observer() {
        viewModel.user.observe(viewLifecycleOwner) {state ->
            when(state) {
                is UiState.Loading -> {
                    binding.progressBarCreatePost.show()
                }
                is UiState.Success -> {
                    binding.progressBarCreatePost.hide()
                    userObj = state.data
                    binding.textViewCreatePostUsername.text = userObj?.user_nick
                    // avatar çekme işlemi ...
                }
                is UiState.Failure -> {
                    binding.progressBarCreatePost.hide()
                    toast(state.error ?: "error")
                }
            }
        }
        viewModel.postfeed.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UiState.Loading -> {
                    binding.progressBarCreatePost.show()
                }
                is UiState.Success -> {
                    binding.progressBarCreatePost.hide()
                    toast(state.data)
                    findNavController().popBackStack()
                }
                is UiState.Failure -> {
                    binding.progressBarCreatePost.hide()
                    toast(state.error ?: "error postfeed")
                }
            }
        }
        viewModel.postExp.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UiState.Loading -> {
                    binding.progressBarCreatePost.show()
                }
                is UiState.Success -> {
                    binding.progressBarCreatePost.hide()
                    toast(state.data)
                    findNavController().popBackStack()
                }
                is UiState.Failure -> {
                    binding.progressBarCreatePost.hide()
                    toast(state.error ?: "error postexp")
                }
            }
        }
    }

    private fun createPostFeed(): PostFeed {
        return PostFeed(
            Date(),
            userObj?.user_avatar,
            0,
            binding.editTextCreatePostContent.text.toString(),
            0,
            userObj?.user_nick,
            binding.tag.text.toString(),
            ""
        )
    }

    private fun createPostExp(): PostExp {
        return PostExp(
            Date(),
            userObj?.user_avatar,
            0,
            binding.editTextCreatePostContent.text.toString(),
            0,
            userObj?.user_nick,
            binding.tag.text.toString(),
            ""
        )
    }

    private fun validation(): Boolean {
        var isValid = true
        if(binding.editTextCreatePostContent.text.isNullOrEmpty()) {
            isValid = false
            toast("Please, enter your feelings.")
        }
        /* tag seçme kontrolü
        if() {
        }
         */
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}