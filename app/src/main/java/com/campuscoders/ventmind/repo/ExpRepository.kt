package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.PostExp
import com.campuscoders.ventmind.util.UiState

interface ExpRepository {
    fun getPosts(result: (UiState<List<PostExp>>) -> Unit)
    fun getPosts(feeling: String, result: (UiState<List<PostExp>>) -> Unit)
    fun checkLike(postId: String, result: (UiState<Boolean>) -> Unit)
    fun updatePostExpLike(postId: String, result: (UiState<String>) -> Unit)
    fun checkDislike(postId: String, result: (UiState<Boolean>) -> Unit)
    fun updatePostExpDislike(postId: String, result: (UiState<String>) -> Unit)
}