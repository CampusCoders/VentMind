package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.PostExp
import com.campuscoders.ventmind.model.PostFeed
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.util.FirestoreCollection
import com.campuscoders.ventmind.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreatePostRepositoryImp(
    val database: FirebaseFirestore,
    val auth: FirebaseAuth,
): CreatePostRepository {

    override fun getUser(result: (UiState<User>) -> Unit) {
        val userId = auth.currentUser?.uid
        val document = database.collection(FirestoreCollection.USER).document(userId.toString())
        document.get()
            .addOnSuccessListener {
                if(it.exists()){
                    val user = it.toObject(User::class.java)
                    result.invoke(
                        UiState.Success(user!!)
                    )
                }else{
                    result.invoke(UiState.Failure("Failed to fetch user inforöation."))
                }
            }
            .addOnFailureListener{
                result.invoke(
                    UiState.Failure(it.localizedMessage)
                )
            }
    }

    override fun addPostFeed(post: PostFeed, result: (UiState<String>) -> Unit) {
        // document = postId
        val document = database.collection(FirestoreCollection.POST_FEED).document()

        post.post_user_id = auth.currentUser?.uid
        post.post_id = document.id

        database.collection(FirestoreCollection.POST_FEED).document(document.id)
            .set(post)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.Success("The post has been sent."))
                }else {
                    result.invoke(UiState.Failure("Adding post operation is failed!"))
                }
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure("Adding post operation is failed"))
            }

        /*
        post.post_user_id = auth.currentUser?.uid
        database.collection(FirestoreCollection.POST_FEED).add(post)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.Success("The post has been sent."))
                }else {
                    result.invoke(UiState.Failure("Adding post operation is failed!"))
                }
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure("Adding post operation is failed"))
            }

         */
    }

    override fun addPostExp(post: PostExp, result: (UiState<String>) -> Unit) {
       post.post_user_id = auth.currentUser!!.uid
        database.collection(FirestoreCollection.POST_EXP).add(post)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.Success("The post has been sent."))
                } else{
                    result.invoke(UiState.Failure("Adding post operation is failed!"))
                }
            }
            .addOnFailureListener{
                result.invoke(UiState.Failure("Adding post operation is failed!"))
            }
    }
}