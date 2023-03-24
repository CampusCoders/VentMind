package com.campuscoders.ventmind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.campuscoders.ventmind.model.PostExp
import com.campuscoders.ventmind.model.PostFeed
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.repo.CreatePostRepository
import com.campuscoders.ventmind.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val repository: CreatePostRepository
): ViewModel() {

    private val _user = MutableLiveData<UiState<User>>()
    val user: LiveData<UiState<User>>
        get() = _user

    private val _postFeed = MutableLiveData<UiState<String>>()
    val postfeed: LiveData<UiState<String>>
        get() = _postFeed

    private val _postExp = MutableLiveData<UiState<String>>()
    val postExp: LiveData<UiState<String>>
        get() = _postExp

    fun getUser() {
        _user.value = UiState.Loading
        repository.getUser {
            _user.value = it
        }
    }

    fun addPostFeed(post: PostFeed) {
        _postFeed.value = UiState.Loading
        repository.addPostFeed(post) {
            _postFeed.value = it
        }
    }

    fun addPostExp(post: PostExp) {
        _postExp.value = UiState.Loading
        repository.addPostExp(post) {
            _postExp.value = it
        }
    }
}