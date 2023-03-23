package com.campuscoders.ventmind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.repo.AuthRepository
import com.campuscoders.ventmind.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    // login işleminin durumunu tutan livedata
    private val _login = MutableLiveData<UiState<String>>()
    val login: LiveData<UiState<String>>
        get() = _login

    private val _register = MutableLiveData<UiState<String>>()
    val register: LiveData<UiState<String>>
        get() = _register

    private val _forgotPassword = MutableLiveData<UiState<String>>()
    val forgotPassword: LiveData<UiState<String>>
        get() = _forgotPassword

    private val _logOut=MutableLiveData<UiState<String>>()
    val logOut:LiveData<UiState<String>>
        get() = _logOut

    private val _deleteAccount=MutableLiveData<UiState<String>>()
    val deleteAccount:LiveData<UiState<String>>
        get() = _deleteAccount

    // repository'deki fonksiyonları çağıran viewmodel fonksiyonları
    fun loginFun(email: String, password: String) {
        _login.value = UiState.Loading
        repository.loginUser(email, password) {
            _login.value = it
        }
    }

    fun registerFun(email: String, password: String, user: User) {
        _register.value = UiState.Loading
        repository.registerUser(email,password,user) {
            _register.value = it
        }
    }

    fun forgotPasswordFun(email: String) {
        _forgotPassword.value = UiState.Loading
        repository.forgotPassword(email) {
            _forgotPassword.value = it
        }
    }

    fun logOutFun(){
        _logOut.value=UiState.Loading
        repository.logOut(){
            _logOut.value=it
        }
    }

    fun deleteAccountFun(){
        _deleteAccount.value=UiState.Loading
        repository.deleteAccount(){
            _deleteAccount.value=it
        }
    }
}