package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.PostExp
import com.campuscoders.ventmind.model.PostFeed
import com.campuscoders.ventmind.model.Trend
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
                    result.invoke(UiState.Failure("Failed to fetch user information."))
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

        val trendName = post.post_tag
        post.post_user_id = auth.currentUser?.uid
        post.post_id = document.id

        database.collection(FirestoreCollection.POST_FEED).document(document.id)
            .set(post)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    increaseTrendCount(trendName?:"") {state ->
                        when(state) {
                            is UiState.Loading -> {}
                            is UiState.Success -> {
                                result.invoke(UiState.Success("The post has been sent."))
                            }
                            is UiState.Failure -> {
                                result.invoke(UiState.Failure(state.error))
                            }
                        }
                    }
                }else {
                    result.invoke(UiState.Failure("Adding post operation is failed!"))
                }
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure("Adding post operation is failed"))
            }
    }

    override fun addPostExp(post: PostExp, result: (UiState<String>) -> Unit) {

        val document = database.collection(FirestoreCollection.POST_EXP).document()
        val trendName = post.post_tag
        post.post_user_id = auth.currentUser?.uid
        post.post_id = document.id

        database.collection(FirestoreCollection.POST_EXP).document(document.id)
            .set(post)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    increaseTrendCount(trendName?:"") {state ->
                        when(state) {
                            is UiState.Loading -> {}
                            is UiState.Success -> {
                                result.invoke(UiState.Success("The post has been sent."))
                            }
                            is UiState.Failure -> {
                                result.invoke(UiState.Failure(state.error))
                            }
                        }
                    }
                }else {
                    result.invoke(UiState.Failure("Adding post operation is failed!"))
                }
            }
            .addOnFailureListener { message ->
                result.invoke(UiState.Failure(message.localizedMessage))
            }
    }

    override fun increaseTrendCount(trendName: String, result: (UiState<String>) -> Unit) {
        val document = database.collection(FirestoreCollection.TREND).document(trendName)
        document.get()
            .addOnSuccessListener {
                if(it.exists()) {
                    val trend = it.toObject(Trend::class.java)
                    val trendCount = trend?.trend_count?.plus(1)

                    // trend update
                    document.update("trend_count",trendCount)
                        .addOnSuccessListener {
                            result.invoke(
                                UiState.Success("")
                            )
                        }
                        .addOnFailureListener {message ->
                            result.invoke(
                                UiState.Failure(message.localizedMessage)
                            )
                        }
                } else {
                    result.invoke(
                        UiState.Failure("data not found")
                    )
                }
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(it.localizedMessage)
                )
            }
    }
}