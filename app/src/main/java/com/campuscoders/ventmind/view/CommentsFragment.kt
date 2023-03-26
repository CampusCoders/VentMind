package com.campuscoders.ventmind.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.campuscoders.ventmind.adapter.CommentsAdapter
import com.campuscoders.ventmind.databinding.FragmentCommentsBinding
import com.campuscoders.ventmind.model.Comment
import com.campuscoders.ventmind.util.UiState
import com.campuscoders.ventmind.util.hide
import com.campuscoders.ventmind.util.show
import com.campuscoders.ventmind.util.toast
import com.campuscoders.ventmind.viewmodel.CommentsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class CommentsFragment: Fragment() {

    private var _binding: FragmentCommentsBinding? = null
    private val binding get() = _binding!!

    val sdf = SimpleDateFormat("dd MMM yyyy")

    val viewModel: CommentsViewModel by viewModels()

    private var rootPostId: String? = null

    private val commentsAdapter by lazy {
        CommentsAdapter(
            onCommentClickListener = { commentId, postId ->
                // comment'e tıklanınca tıklanılan commentId ve root_postId yollanır.
                println(commentId + postId)
                viewModel.giveAwardFun(commentId,postId)
            }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCommentsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recyclewView
        binding.recyclerViewComments.adapter = commentsAdapter
        binding.recyclerViewComments.layoutManager = LinearLayoutManager(requireContext())

        // comment send
        binding.imageViewSendComment.setOnClickListener {
            if(validation()) {
                viewModel.setCommentFun(createComment(),rootPostId?:"")
            } else {
                toast("Please, enter your comment.")
            }
        }

        val postId = arguments?.getString("post_id",null)
        postId?.let {
            // ekrana post ve ona bağlı commentler getirilir.
            viewModel.getCommentsFun(it)
            viewModel.getRootPostFun(it)
            rootPostId = it
        }

        observer()
    }

    private fun observer() {
        viewModel.comments.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UiState.Loading -> {
                    binding.progressBarComments.show()
                }
                is UiState.Success -> {
                    binding.progressBarComments.hide()
                    // liste adapter'a verilir
                    println("commentsler geldi")
                    commentsAdapter.updateList(state.data.toMutableList())
                }
                is UiState.Failure -> {
                    binding.progressBarComments.hide()
                }
            }
        }
        viewModel.rootPost.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UiState.Loading -> {
                    binding.progressBarComments.show()
                }
                is UiState.Success -> {
                    binding.progressBarComments.hide()
                    val post = state.data
                    // set ui
                    binding.textViewCommentsUsername.text = post.post_nick
                    binding.textViewCommentsDate.text = post.created_at?.let { sdf.format(it) }
                    binding.textViewCommentsTag.text = post.post_tag
                    binding.textViewCommentsContent.text = post.post_content
                    binding.textViewCommentsLikeCount.text = post.post_like_count.toString()
                    binding.textViewCommentsCommentCount.text = post.post_comment_count.toString()
                    // avatar glide
                }
                is UiState.Failure -> {
                    binding.progressBarComments.hide()
                }
            }
        }
        viewModel.comment.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UiState.Loading -> {
                    binding.progressBarComments.show()
                }
                is UiState.Success -> {
                    binding.progressBarComments.hide()
                    toast("Comment has been sent.")
                    findNavController().popBackStack()
                }
                is UiState.Failure -> {
                    binding.progressBarComments.hide()
                    toast(state.error?:"error")
                }
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = true
        if(binding.editTextComments.text.toString().isNullOrEmpty()) {
            isValid = false
        }
        return isValid
    }

    private fun createComment(): Comment {
        return Comment(
            "",
            false,
            binding.editTextComments.text.toString(),
            "",
            "",
            "",
            ""
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}