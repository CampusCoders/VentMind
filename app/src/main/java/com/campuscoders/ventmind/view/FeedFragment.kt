package com.campuscoders.ventmind.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.campuscoders.ventmind.MainActivity
import com.campuscoders.ventmind.R
import com.campuscoders.ventmind.adapter.FeedAdapter
import com.campuscoders.ventmind.databinding.FragmentFeedBinding
import com.campuscoders.ventmind.util.UiState
import com.campuscoders.ventmind.util.hide
import com.campuscoders.ventmind.util.show
import com.campuscoders.ventmind.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment: Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    val viewModel: FeedViewModel by viewModels()
    private val feedAdapter by lazy {
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
                // like sayısı güncellenir
                viewModel.updatePostCount(it)
            },
            commentOnItemClickListener = {
                // CommentsFragment'a gider
                findNavController().navigate(R.id.action_feedFragment_to_commentsFragment, Bundle().apply {
                    putString("post_id",it)
                })
            }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFeedBinding.inflate(inflater,container,false)

        val feelings = resources.getStringArray(R.array.feelingsFeed)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, feelings)
        binding.autoComplete.setAdapter(arrayAdapter)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? MainActivity)?.setSupportActionBar(binding.toolbarFeed)

        // spinner
        binding.autoComplete.addTextChangedListener {
            val tag = it.toString()
            if (tag == "Home") {
                viewModel.getPosts()
            } else {
                viewModel.getPosts(tag)
            }
        }

        // recyclerView
        binding.recyclerViewFeed.adapter = feedAdapter
        binding.recyclerViewFeed.layoutManager = LinearLayoutManager(requireContext())

        // get datas
        viewModel.getPosts()

        observer()

        // setOnClickListeners:
        binding.fabFeed.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_createPostFragment, Bundle().apply {
                putString("control","feed")
            })
        }
        binding.trendFragment.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_trendFragment)
        }
        binding.experienceFragment.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_experienceFragment)
        }
        binding.profileFragment.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_profileFragment, Bundle().apply {
                putString("user_id","")
            })
        }
        binding.userListFragment.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_userListFragment)
        }
        binding.settingsFragment.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_settingsFragment)
        }
    }

    private fun observer() {
        viewModel.posts.observe(viewLifecycleOwner) { state ->
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
        viewModel.updatePost.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UiState.Loading -> {
                    binding.progressBarFeed.show()
                }
                is UiState.Success -> {
                    binding.progressBarFeed.hide()
                    if(state.data) {
                        // like'ı bir arttır
                        feedAdapter.updateLikeCount(true)
                    } else {
                        // like'ı bir azalt
                        feedAdapter.updateLikeCount(false)
                    }
                }
                is UiState.Failure -> {
                    binding.progressBarFeed.hide()
                }
            }
        }
        viewModel.postsByTag.observe(viewLifecycleOwner) {state ->
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}