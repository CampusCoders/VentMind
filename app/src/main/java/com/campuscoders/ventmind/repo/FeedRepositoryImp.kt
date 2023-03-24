package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.LikeFeed
import com.campuscoders.ventmind.model.PostFeed
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
        val userId = auth.currentUser?.uid
        val likeFeedId = userId + postId

        println(likeFeedId)

        val document = database.collection(FirestoreCollection.LIKE_FEED).document(userId?:"")
        document.get()
            .addOnSuccessListener {
                if (it.exists()) {
                    // like atılmış
                    result.invoke(UiState.Success(true))
                } else {
                    // like atılmamış

                    val likeFeed = LikeFeed(postId,userId)
                    document.set(likeFeed)
                        .addOnCompleteListener {
                            result.invoke(UiState.Success(false))
                        }
                        .addOnFailureListener {
                            result.invoke(UiState.Failure("Adding FeedLike error"))
                        }
                }
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun updateLikeCount(postId: String, result: (UiState<String>) -> Unit) {
        val document = database.collection(FirestoreCollection.POST_FEED).document(postId)
        document.get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val postFeed = it.toObject(PostFeed::class.java)
                    var likeCount = postFeed?.post_like_count
                    // check user'a göre arttırma azaltma
                    checkLike(postId) {state ->
                        when(state) {
                            is UiState.Loading -> {}
                            is UiState.Success -> {
                                if(state.data) {    // like atılmış

                                    // like azaltıldı
                                    if(likeCount != null) { likeCount-- }

                                    // postFeed güncelleme
                                    document.update("post_like_count", likeCount)
                                        .addOnCompleteListener {
                                            result.invoke(UiState.Success("Like count has been reduced"))
                                        }
                                        .addOnFailureListener {
                                            result.invoke(UiState.Failure("Like count process error"))
                                        }

                                } else {    // like atılmamış

                                    // like arttırıldı
                                    if(likeCount != null) { likeCount++ }

                                    // postFeed güncelleme
                                    document.update("post_like_count", likeCount)
                                        .addOnCompleteListener {
                                            result.invoke(UiState.Success("Like count has been increased"))
                                        }
                                        .addOnFailureListener {
                                            result.invoke(UiState.Failure("Like count process error"))
                                        }
                                }
                            }
                            is UiState.Failure -> {

                            }
                        }
                    }

                } else {
                    result.invoke(UiState.Failure("ERROR!!!"))
                }
            }
            .addOnFailureListener {

            }
    }

}