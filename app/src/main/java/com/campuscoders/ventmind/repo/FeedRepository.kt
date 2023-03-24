package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.PostFeed
import com.campuscoders.ventmind.util.UiState

interface FeedRepository {
    fun getPosts(result: (UiState<List<PostFeed>>) -> Unit)
    fun getPosts(feeling: String, result: (UiState<List<PostFeed>>) -> Unit)
    fun checkLike(postId: String, result: (UiState<Boolean>) -> Unit)
    fun updateLikeCount(postId: String, result: (UiState<Boolean>) -> Unit)
}