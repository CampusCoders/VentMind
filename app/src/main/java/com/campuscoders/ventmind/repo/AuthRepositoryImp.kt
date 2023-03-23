package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.util.FirestoreCollection
import com.campuscoders.ventmind.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

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
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    updateUserInfo(user) {state ->
                        when(state) {
                            is UiState.Success -> {
                                result.invoke(UiState.Success("User registered successfully."))
                            }
                            is UiState.Failure -> {
                                result.invoke(UiState.Failure(state.error))
                            }
                            else -> {}
                        }
                    }
                } else {
                    try {
                        throw it.exception ?: java.lang.Exception("Invalid authentication.")
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        result.invoke(UiState.Failure("Authentication failed, password should be at least 6 characters."))
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        result.invoke(UiState.Failure("Authentication failed, invalid email entered."))
                    } catch (e: FirebaseAuthUserCollisionException) {
                        result.invoke(UiState.Failure("Authentication failed, email already registered."))
                    } catch (e: Exception) {
                        result.invoke(UiState.Failure(e.message))
                    }
                }
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(it.localizedMessage)
                )
            }
    }

    override fun updateUserInfo(user: User, result: (UiState<String>) -> Unit) {
        val document = database.collection(FirestoreCollection.USER).document(
            auth.currentUser?.uid ?: "error"
        )

        document.set(user)
            .addOnSuccessListener {
                result.invoke(UiState.Success("User has been created successfully."))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun forgotPassword(email: String, result: (UiState<String>) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    result.invoke(UiState.Success("Email has been sent"))
                } else {
                    result.invoke(UiState.Failure(it.exception?.message))
                }
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure("Authentication failed, check email.")
                )
            }
    }

    override fun updateUsername(username: String, result: (UiState<String>) -> Unit) {

        val document = database.collection(FirestoreCollection.USER).document(
            auth.currentUser?.uid ?: "error"
        )

        document.update("user_nick",username)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    result.invoke(UiState.Success("Username has been updated"))
                }else{
                    result.invoke((UiState.Failure(it.exception?.message)))
                }
            }
            .addOnFailureListener{
                result.invoke(UiState.Failure("Authentication failed"))
            }
    }

    override fun logOut(result: (UiState<String>) -> Unit) {
        val user = auth.currentUser
        if(user != null){
            auth.signOut()
            result.invoke(UiState.Success("User has been sign out"))
        }
    }

    override fun deleteAccount(result: (UiState<String>) -> Unit) {
        val user = database.collection(FirestoreCollection.USER).document(
            auth.currentUser?.uid?:"error")

        if (user != null){
            user.delete()
                .addOnCompleteListener{
                    result.invoke(UiState.Success("User has been deleted"))
                }
                .addOnFailureListener{
                    result.invoke(UiState.Failure("Delete operation failed"))
                }
        }else{
            result.invoke(UiState.Failure("Authentication failed"))
        }
    }
}