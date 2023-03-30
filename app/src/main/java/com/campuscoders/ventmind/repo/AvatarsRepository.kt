package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.Avatar
import com.campuscoders.ventmind.util.UiState

interface AvatarsRepository {
    fun getAvatars(result: (UiState<List<Avatar>>) -> Unit)
    fun getUserScore(result: (UiState<Int>) -> Unit)
    fun changeAvatar(avatarSource: String ,result: (UiState<String>) -> Unit)
}