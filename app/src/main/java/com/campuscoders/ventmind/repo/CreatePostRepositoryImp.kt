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

    override fun getUser(userId:String ,result: (UiState<User>) -> Unit) {
        val userId = auth.currentUser
        val document = database.collection(FirestoreCollection.USER).document(userId.toString())
        document.get()
            .addOnSuccessListener {
                if(it.exists()){
                    val user = it.toObject(User::class.java)
                    result.invoke(
                        UiState.Success(user!!)
                    )
                }else{
                    result.invoke(UiState.Failure("error"))
                }
            }
            .addOnFailureListener{
                result.invoke(
                    UiState.Failure(it.localizedMessage)
                )
            }
    }

    override fun addPostFeed(post: PostFeed, result: (UiState<String>) -> Unit) {
        post.post_user_id = auth.currentUser!!.uid
        database.collection(FirestoreCollection.POST_FEED).add(post)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    result.invoke(UiState.Success("Post has been added"))
                }else{
                    result.invoke(UiState.Failure("Post add operation failed"))
                }
            }
            .addOnFailureListener{
                result.invoke(UiState.Failure("Post add operation failed"))
            }
    }

    override fun addPostExp(post: PostExp, result: (UiState<String>) -> Unit) {
        post.post_user_id = auth.currentUser!!.uid
        database.collection(FirestoreCollection.POST_EXP).add(post)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    result.invoke(UiState.Success("Post has been added"))
                }else{
                    result.invoke(UiState.Failure("Post add operation failed"))
                }
            }
            .addOnFailureListener{
                result.invoke(UiState.Failure("Post add operation failed"))
            }
    }
}