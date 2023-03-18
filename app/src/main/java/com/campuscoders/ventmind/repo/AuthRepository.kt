package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.util.UiState

interface AuthRepository {
    fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit)
    fun registerUser(email: String, password: String, user: User, result: (UiState<String>) -> Unit)
    fun updateUserInfo(user: User, result: (UiState<String>) -> Unit)
    fun forgotPassword(email: String, result: (UiState<String>) -> Unit)
    fun updateUsername(username: String, result: (UiState<String>) -> Unit)
    fun logOut()
    fun deleteAccount()
}