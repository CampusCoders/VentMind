package com.campuscoders.ventmind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.campuscoders.ventmind.model.PostExp
import com.campuscoders.ventmind.repo.ExpRepository
import com.campuscoders.ventmind.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExpViewModel @Inject constructor(
    private val repository: ExpRepository
): ViewModel() {

    private val _posts = MutableLiveData<UiState<List<PostExp>>>()
    val posts: LiveData<UiState<List<PostExp>>>
        get() = _posts

    fun getPosts() {
        _posts.value = UiState.Loading
        repository.getPosts {
            _posts.value = it
        }
    }
}