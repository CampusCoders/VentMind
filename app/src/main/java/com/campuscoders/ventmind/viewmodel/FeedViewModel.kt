package com.campuscoders.ventmind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.campuscoders.ventmind.model.PostFeed
import com.campuscoders.ventmind.repo.FeedRepository
import com.campuscoders.ventmind.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repository: FeedRepository
): ViewModel() {

    private val _posts = MutableLiveData<UiState<List<PostFeed>>>()
    val posts: LiveData<UiState<List<PostFeed>>>
        get() = _posts

    private val _updatePost = MutableLiveData<UiState<Boolean>>()
    val updatePost: LiveData<UiState<Boolean>>
        get() = _updatePost

    fun getPosts() {
        _posts.value = UiState.Loading
        repository.getPosts {
            _posts.value = it
        }
    }

    fun updatePostCount(postId: String) {
        _updatePost.value = UiState.Loading
        repository.updateLikeCount(postId) {
            _updatePost.value = it
        }
    }

}