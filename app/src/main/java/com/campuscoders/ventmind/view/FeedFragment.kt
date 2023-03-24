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
import com.campuscoders.ventmind.adapter.FeedAdapter
import com.campuscoders.ventmind.databinding.FragmentFeedBinding
import com.campuscoders.ventmind.util.UiState
import com.campuscoders.ventmind.util.hide
import com.campuscoders.ventmind.util.show
import com.campuscoders.ventmind.util.toast
import com.campuscoders.ventmind.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment: Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    val viewModel: FeedViewModel by viewModels()
    val feedAdapter by lazy {
        FeedAdapter(
            avatarOnItemClickListener = {
                // profil ekranına gider
                findNavController().navigate(R.id.action_feedFragment_to_profileFragment, Bundle().apply {
                    putString("user_id",it)
                })
            },
            usernameOnItemClickListener = {
                // profil ekranına gider
                findNavController().navigate(R.id.action_feedFragment_to_profileFragment, Bundle().apply {
                    putString("user_id",it)
                })
            },
            likeOnItemClickListener = {
                toast(it)
            },
            commentOnItemClickListener = {
                toast(it)
            }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFeedBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recyclerView
        binding.recyclerViewFeed.adapter = feedAdapter
        binding.recyclerViewFeed.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getPosts()

        observer()

        binding.fabFeed.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_createPostFragment)
        }

    }

    private fun observer() {
        viewModel.posts.observe(viewLifecycleOwner) {state ->
            when(state) {
                is UiState.Loading -> {
                    binding.progressBarFeed.show()
                }
                is UiState.Success -> {
                    binding.progressBarFeed.hide()
                    feedAdapter.updateList(state.data.toMutableList())
                }
                is UiState.Failure -> {
                    binding.progressBarFeed.hide()
                }
            }
        }

        /*
        viewModel.checklike.observe(viewLifecycleOwner) {state ->
            when(state) {
                is UiState.Loading -> {
                    binding.progressBarFeed.show()
                }
                is UiState.Success -> {
                    binding.progressBarFeed.hide()
                }
                is UiState.Failure -> {
                    binding.progressBarFeed.hide()

                }
            }
        }

         */
    }

    /*
    private fun validation(): Boolean {
        var isValid=true

        return isValid
    }

     */

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}