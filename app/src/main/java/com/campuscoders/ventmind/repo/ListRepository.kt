package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.Trend
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.util.UiState

interface ListRepository {
    fun getUsers(result: (UiState<List<User>>) -> Unit)
    fun getTrends(result: (UiState<List<Trend>>) -> Unit)
}