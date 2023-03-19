package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepositoryImp(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore
): AuthRepository {

    override fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    result.invoke(UiState.Success("Login Successfully."))
                }
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure("Authentication failed."))
            }
    }

    override fun registerUser(email: String, password: String, user: User, result: (UiState<String>) -> Unit) {
        // ilk önce register işlemi yapılır. işlem başarılı olursa alınan user objesi updateUserInfo'ya verilir
    }

    override fun updateUserInfo(user: User, result: (UiState<String>) -> Unit) {
        // user objesi veritabanına auth'dan çekilen user id'si ile kaydedilir.
    }

    override fun forgotPassword(email: String, result: (UiState<String>) -> Unit) {
        // kullanıcıya şifre resetleme maili gönderilir.
    }

    override fun updateUsername(username: String, result: (UiState<String>) -> Unit) {
        // alınan username, kullanıcının id'si ile çekilen User document'inde güncellenir
    }

    override fun logOut() {

    }

    override fun deleteAccount() {

    }
}