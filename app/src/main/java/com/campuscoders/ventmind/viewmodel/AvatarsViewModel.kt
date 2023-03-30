package com.campuscoders.ventmind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.campuscoders.ventmind.model.Avatar
import com.campuscoders.ventmind.repo.AvatarsRepository
import com.campuscoders.ventmind.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AvatarsViewModel @Inject constructor(
    private val repository: AvatarsRepository
): ViewModel() {
    private val _avatars = MutableLiveData<UiState<List<Avatar>>>()
    val avatar : LiveData<UiState<List<Avatar>>>
        get() = _avatars

    private val _userScore = MutableLiveData<UiState<Int>>()
    val userScore : LiveData<UiState<Int>>
        get() = _userScore

    private val _changedAvatar = MutableLiveData<UiState<String>>()
    val changedAvatar: LiveData<UiState<String>>
        get() = _changedAvatar

    fun getAvatarsFun(){
        _avatars.value = UiState.Loading
        repository.getAvatars (){
            _avatars.value = it
        }
    }

    fun getUserScore(){
        _userScore.value = UiState.Loading
        repository.getUserScore (){
            _userScore.value = it
        }
    }

    fun changeAvatar(avatarSource: String) {
        _changedAvatar.value = UiState.Loading
        repository.changeAvatar(avatarSource) {
            _changedAvatar.value = it
        }
    }

}