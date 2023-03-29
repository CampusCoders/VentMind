package com.campuscoders.ventmind.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.campuscoders.ventmind.R
import com.campuscoders.ventmind.databinding.FragmentCreatePostBinding
import com.campuscoders.ventmind.model.PostExp
import com.campuscoders.ventmind.model.PostFeed
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.util.*
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

        val feelings = resources.getStringArray(R.array.feelings)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, feelings)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set ui
        val userId = arguments?.getString("control",null)
        if(userId != null) {
            if(userId == "feed") {
                binding.buttonShareExperience.hide()
            } else {
                binding.buttonShareFeeling.hide()
            }
        }

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
                    binding.imageViewCreatePostAvatar.downloadFromUrl(state.data.user_avatar, placeHolderProgressBar(requireContext()))
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
            binding.autoCompleteTextView.text.toString(),
            "",
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
            binding.autoCompleteTextView.text.toString(),
            "",
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