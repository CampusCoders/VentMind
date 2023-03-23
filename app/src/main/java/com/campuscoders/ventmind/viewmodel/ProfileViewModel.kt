package com.campuscoders.ventmind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.campuscoders.ventmind.model.PostFeed
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.repo.ProfileRepository
import com.campuscoders.ventmind.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
): ViewModel() {

    private val _user = MutableLiveData<UiState<User>>()
    val user: LiveData<UiState<User>>
        get() = _user

    private val _post = MutableLiveData<UiState<List<PostFeed>>>()
    val post: LiveData<UiState<List<PostFeed>>>
        get() = _post

    private val _bio = MutableLiveData<UiState<String>>()
    val bio: LiveData<UiState<String>>
        get() = _bio

    private val _checkUser = MutableLiveData<Boolean>()
    val checkUser: LiveData<Boolean>
        get() = _checkUser


    fun getUser(userId: String) {
        _user.value = UiState.Loading
        repository.getUser(userId) {
            _user.value = it
        }
    }

    fun getPosts(userId: String) {
        _post.value = UiState.Loading
        repository.getUserPosts(userId) {
            _post.value = it
        }
    }

    fun setBio(bio: String) {
        _bio.value = UiState.Loading
        repository.setUserBio(bio) {
            _bio.value = it
        }
    }

    fun checkUser(userId: String) {
        _checkUser.value = false
        repository.checkUser(userId) {
            _checkUser.value = it
        }
    }
}