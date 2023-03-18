package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.PostFeed
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.util.UiState

interface ProfileRepository {
    fun getUser(result: (UiState<User>) -> Unit)
    fun getUserPosts(result: (UiState<List<PostFeed>>) -> Unit)
    fun setUserBio(bio: String, result: (UiState<String>) -> Unit)
}