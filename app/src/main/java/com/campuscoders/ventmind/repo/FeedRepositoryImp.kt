package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.LikeFeed
import com.campuscoders.ventmind.model.PostFeed
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.util.FirestoreCollection
import com.campuscoders.ventmind.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FeedRepositoryImp(
    val database: FirebaseFirestore,
    val auth: FirebaseAuth,
): FeedRepository {
    override fun getPosts(result: (UiState<List<PostFeed>>) -> Unit) {
        database.collection(FirestoreCollection.POST_FEED).get()
            .addOnSuccessListener {
                val postFeedList = arrayListOf<PostFeed>()
                for(document in it) {
                    val postFeed = document.toObject(PostFeed::class.java)
                    postFeedList.add(postFeed)
                }
                result.invoke(
                    UiState.Success(postFeedList)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(it.localizedMessage)
                )
            }
    }

    override fun getPosts(feeling: String, result: (UiState<List<PostFeed>>) -> Unit) {
        // PostFeed'den "post_tag"e göre filtrelenmiş veriler çekilir.
    }

    override fun checkLike(postId: String, result: (UiState<Boolean>) -> Unit) {
        // kullanıcı id'si ile alınan postId birleştirilip LikeFeed'de arama yapılır. Sonuca göre boolean ifade döndürür.
        val userId=auth.currentUser?.uid
        var controller =false
        database.collection(FirestoreCollection.LIKE_FEED).get()
            .addOnSuccessListener {
                val likeFeedList = arrayListOf<LikeFeed>()
                for (document in it){
                    val likeFeed=document.toObject(LikeFeed::class.java)
                    likeFeedList.add(likeFeed)
                }
                for (like in likeFeedList){
                    if (like.user_id==userId && like.post_id==postId){
                        controller=true
                        break
                    }
                }
                if (controller){
                    result.invoke(UiState.Success(false))
                }else {
                    result.invoke(UiState.Success(true))
                }
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun updatePostFeedLike(postId: String, result: (UiState<String>) -> Unit) {
        // checkLike'ın boolean sonucuna göre PostFeed'de "post_like_count" değeri arttırılır veya azaltılır.
    }
    override fun liked(liked:LikeFeed,result: (UiState<String>) -> Unit) {
        liked.user_id=auth.currentUser?.uid
        database.collection(FirestoreCollection.LIKE_FEED).add(liked)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    result.invoke(UiState.Success("The liked has been sent."))

                }else{
                    result.invoke(UiState.Failure("Adding liked operation is failed"))
                }
            }
            .addOnFailureListener {
                result.invoke((UiState.Failure("Adding liked operation is failed")))
            }
    }
}