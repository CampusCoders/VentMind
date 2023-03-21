package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.PostFeed
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.util.FirestoreCollection
import com.campuscoders.ventmind.util.UiState
import com.google.firebase.firestore.FirebaseFirestore

class ProfileRepositoryImp(
    val database: FirebaseFirestore
): ProfileRepository {

    override fun getUser(userId: String, result: (UiState<User>) -> Unit) {
        println(userId)
        val document = database.collection(FirestoreCollection.USER).document(userId)
        document.get()
            .addOnSuccessListener {
                if(it.exists()) {
                    val user = it.toObject(User::class.java)
                    result.invoke(
                        UiState.Success(user!!)
                    )
                } else {
                    result.invoke(
                        UiState.Failure("Error!")
                    )
                }
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(it.localizedMessage)
                )
            }
    }

    override fun getUserPosts(userId: String, result: (UiState<List<PostFeed>>) -> Unit) {
        // kullanıcı id'sine göre PostFeed'den veriler çekilir. (kullanıcının kendi postları)
    }

    override fun setUserBio(bio: String, result: (UiState<String>) -> Unit) {
        // kullanıcı id'sine göre User'daki bio güncellenir.
    }
}