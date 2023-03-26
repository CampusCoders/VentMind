package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.Comment
import com.campuscoders.ventmind.model.PostFeed
import com.campuscoders.ventmind.util.UiState

interface CommentsRepository {
    fun setComment(comment: Comment, rootPostId: String, result: (UiState<String>) -> Unit)
    fun getRootPost(postId: String, result: (UiState<PostFeed>) -> Unit)
    fun checkOwnPost(postId: String, result: (UiState<Boolean>) -> Unit)
    fun giveAward(commentId: String,postId: String ,result: (UiState<Boolean>) -> Unit)
    fun updateUserScore(userId: String, control: Boolean, result: (UiState<String>) -> Unit)
    fun increaseCommentCount(postId: String, result: (UiState<String>) -> Unit)
    fun getComments(postId: String, result: (UiState<List<Comment>>) -> Unit)
}