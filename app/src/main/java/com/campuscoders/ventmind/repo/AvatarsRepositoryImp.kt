package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.Avatar
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.util.FirestoreCollection
import com.campuscoders.ventmind.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AvatarsRepositoryImp(
    val database: FirebaseFirestore,
    val auth: FirebaseAuth
    ): AvatarsRepository {

    override fun getAvatars(result: (UiState<List<Avatar>>) -> Unit) {
        database.collection(FirestoreCollection.AVATAR).get()
            .addOnSuccessListener {
                val avatarList = arrayListOf<Avatar>()
                for(document in it){
                    val avatar = document.toObject(Avatar::class.java)
                    avatarList.add(avatar)
                }
                result.invoke(UiState.Success(avatarList))
            }
            .addOnFailureListener{
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun getUserScore(result: (UiState<Int>) -> Unit) {
        val document = database.collection(FirestoreCollection.USER).document(
            auth.currentUser?.uid ?: "")

        document.get()
            .addOnSuccessListener {
                if (it.exists()){
                    val user = it.toObject(User::class.java)
                    val user_score = user!!.user_score
                    result.invoke(UiState.Success(user_score!!))
                }else{
                    result.invoke(UiState.Failure("UserScore field not found"))
                }
            }
            .addOnFailureListener{
                result.invoke(UiState.Failure("Authentication failed"))
            }
    }

    override fun changeAvatar(avatarSource: String ,result: (UiState<String>) -> Unit) {
        val userId = auth.currentUser?.uid
        database.collection(FirestoreCollection.USER).document(userId?:"")
            .update("user_avatar",avatarSource)
            .addOnSuccessListener {
                changeAvatarOnPosts(avatarSource,userId.toString()) {state->
                    when(state) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            changeAvatarOnComments(avatarSource,userId.toString()) {st ->
                                when(st) {
                                    is UiState.Loading -> {}
                                    is UiState.Success -> {
                                        result.invoke(UiState.Success(state.data))
                                    }
                                    is UiState.Failure -> {
                                        result.invoke(UiState.Failure(st.error))
                                    }
                                }
                            }
                        }
                        is UiState.Failure -> {
                            result.invoke(UiState.Failure(state.error))
                        }
                    }
                }
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun changeAvatarOnPosts(avatarSource: String, userId: String, result: (UiState<String>) -> Unit) {
        database.collection(FirestoreCollection.POST_FEED).whereEqualTo("post_user_id",userId).get()
            .addOnSuccessListener {
                for (document in it) {
                    val postId = document["post_id"]
                    database.collection(FirestoreCollection.POST_FEED).document(postId.toString())
                        .update("post_avatar",avatarSource)
                }
                result.invoke(UiState.Success("PostFeed's avatar sources have been updated."))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun changeAvatarOnComments(avatarSource: String, userId: String, result: (UiState<String>) -> Unit) {
        database.collection(FirestoreCollection.COMMENT).whereEqualTo("comment_user_id",userId).get()
            .addOnSuccessListener {
                for (document in it) {
                    val commentId = document["comment_id"]
                    database.collection(FirestoreCollection.COMMENT).document(commentId.toString())
                        .update("comment_avatar",avatarSource)
                }
                result.invoke(UiState.Success("Comment's avatar sources have been updated."))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }
}