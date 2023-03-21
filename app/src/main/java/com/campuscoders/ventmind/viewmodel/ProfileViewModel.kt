package com.campuscoders.ventmind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    fun getUser(userId: String) {
        _user.value = UiState.Loading
        repository.getUser(userId) {
            _user.value = it
        }
    }
}