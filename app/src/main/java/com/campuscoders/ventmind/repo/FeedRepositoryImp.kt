package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.PostFeed
import com.campuscoders.ventmind.util.FirestoreCollection
import com.campuscoders.ventmind.util.UiState
import com.google.firebase.firestore.FirebaseFirestore

class FeedRepositoryImp(
    val database: FirebaseFirestore
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
    }

    override fun updatePostFeedLike(postId: String, result: (UiState<String>) -> Unit) {
        // checkLike'ın boolean sonucuna göre PostFeed'de "post_like_count" değeri arttırılır veya azaltılır.
    }
}