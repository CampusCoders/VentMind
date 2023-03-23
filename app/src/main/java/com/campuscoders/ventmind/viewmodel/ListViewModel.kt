package com.campuscoders.ventmind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.campuscoders.ventmind.model.Trend
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.repo.ListRepository
import com.campuscoders.ventmind.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
        private val repository: ListRepository
): ViewModel() {
    private val _trend = MutableLiveData<UiState<List<Trend>>>()
    val trend: LiveData<UiState<List<Trend>>>
        get() = _trend

    private val _users = MutableLiveData<UiState<List<User>>>()
    val users: LiveData<UiState<List<User>>>
        get() = _users

    fun getTrends() {
        _trend.value = UiState.Loading
        repository.getTrends {
            _trend.value = it
        }
    }

    fun getUsers() {
        _users.value = UiState.Loading
        repository.getUsers {
            _users.value = it
        }
    }
}