package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.PostFeed
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.util.FirestoreCollection
import com.campuscoders.ventmind.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileRepositoryImp(
    val database: FirebaseFirestore,
    val auth: FirebaseAuth
): ProfileRepository {

    override fun getUser(userId: String, result: (UiState<User>) -> Unit) {
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
        database.collection(FirestoreCollection.POST_FEED).whereEqualTo("post_user_id", userId).get()
            .addOnSuccessListener {
                val postFeedListById = arrayListOf<PostFeed>()
                for(document in it) {
                    val postFeed = document.toObject(PostFeed::class.java)
                    postFeedListById.add(postFeed)
                }
                result.invoke(
                    UiState.Success(postFeedListById)
                )
            }
            .addOnFailureListener{
                result.invoke(
                    UiState.Failure(it.localizedMessage)
                )
            }
    }

    override fun setUserBio(bio: String, result: (UiState<String>) -> Unit) {
        val userId = auth.currentUser?.uid
        val document = database.collection(FirestoreCollection.USER).document(userId.toString())
        document.update("user_bio",bio)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Bio has been updated")
                )
            }
            .addOnFailureListener {
                println(it.localizedMessage)
                result.invoke(
                    UiState.Failure(it.localizedMessage)
                )
            }
    }

    override fun checkUser(userId: String ,result: (Boolean) -> Unit) {
        /*
        val user = auth.currentUser
        if(user == null) {
            result.invoke(true)
        } else {
            result.invoke(false)
        }

         */
        val currentUserId = auth.currentUser?.uid
        if(userId == currentUserId) {
            // tıklanılan post'un id'si ile login olan kullanıcının id'si eşit ise
            result.invoke(true)
        } else {
            result.invoke(false)
        }
    }
}