package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.PostExp
import com.campuscoders.ventmind.model.PostFeed
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.util.UiState

interface CreatePostRepository {
    fun getUser(result: (UiState<User>) -> Unit)
    fun addPostFeed(post: PostFeed, result: (UiState<String>) -> Unit)
    fun addPostExp(post: PostExp, result: (UiState<String>) -> Unit)
    fun increaseTrendCount(trendName: String, result: (UiState<String>) -> Unit)
}