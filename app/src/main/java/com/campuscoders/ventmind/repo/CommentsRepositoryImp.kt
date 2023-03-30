package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.Comment
import com.campuscoders.ventmind.model.PostFeed
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.util.FirestoreCollection
import com.campuscoders.ventmind.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CommentsRepositoryImp(
    val database: FirebaseFirestore,
    val auth: FirebaseAuth,
    ): CommentsRepository {

    override fun setComment(comment: Comment, rootPostId: String, result: (UiState<String>) -> Unit) {
        // Comment'e yeni bir document eklenir

        val userId = auth.currentUser?.uid
        val document = database.collection(FirestoreCollection.COMMENT).document()

        // User'daki veriler çekilir.
        database.collection(FirestoreCollection.USER).document(userId?:"")
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val user = it.toObject(User::class.java)
                    comment.comment_id = document.id
                    comment.comment_rootpost_id = rootPostId
                    comment.comment_user_id = userId
                    comment.comment_avatar = user?.user_avatar
                    comment.comment_user_nick = user?.user_nick

                    // comment set işlemi
                    document.set(comment)
                        .addOnSuccessListener {
                            // İlgili post'un comment sayısını arttırma
                            increaseCommentCount(rootPostId) {state ->
                                when(state) {
                                    is UiState.Loading -> {
                                        result.invoke(
                                            UiState.Loading
                                        )
                                    }
                                    is UiState.Success -> {
                                        result.invoke(
                                            UiState.Success("Comment has been added.")
                                        )
                                    }
                                    is UiState.Failure -> {
                                        result.invoke(
                                            UiState.Failure(state.error)
                                        )
                                    }
                                }
                            }
                        }
                        .addOnFailureListener {message ->
                            result.invoke(
                                UiState.Failure(message.localizedMessage)
                            )
                        }
                } else {
                    result.invoke(
                        UiState.Failure("Failed to fetch user infos.")
                    )
                }
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(it.localizedMessage)
                )
            }
    }

    override fun getRootPost(postId: String, result: (UiState<PostFeed>) -> Unit) {
        database.collection(FirestoreCollection.POST_FEED).document(postId).get()
            .addOnSuccessListener {
                val post = it.toObject(PostFeed::class.java)
                result.invoke(UiState.Success(post!!))
            }
            .addOnFailureListener{
                result.invoke(UiState.Failure("Post not found"))
            }
    }

    override fun checkOwnPost(postId: String, result: (UiState<Boolean>) -> Unit) {
        // alınan postId kullanılarak "post_user_id" ile auth.currentUser karşılaştırılır ve sonuç boolean döner.
        // kullanıcı kendi postuna mı bakıyor kontrol edilir.
        val currentUserId = auth.currentUser?.uid
        database.collection(FirestoreCollection.POST_FEED).document(postId).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val post = it.toObject(PostFeed::class.java)
                    val postUserId = post?.post_user_id
                    if(currentUserId == postUserId) {
                        // login user ile root post'un sahibi aynı kişi
                        result.invoke(UiState.Success(true))
                    } else {
                        result.invoke(UiState.Success(false))
                    }
                } else {
                    result.invoke(
                        UiState.Failure("post yok")
                    )
                }
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(it.localizedMessage)
                )
            }
    }

    override fun giveAward(commentId: String,postId: String, control: Boolean,result: (UiState<Boolean>) -> Unit) {
        if(control) {
            // kendi postu
            // tıklanılan comment çekilir
            val document = database.collection(FirestoreCollection.COMMENT).document(commentId)
            document.get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        // comment objesi alınır
                        val comment = it.toObject(Comment::class.java)
                        var awardControl = comment?.comment_award
                        val commentUserId = comment?.comment_user_id
                        if(awardControl != null) {
                            // eğer ödül verilmişse(true) awardControl'a false atanır
                            // eğer ödül verilmemişse(false) awardControl'a true atanır.
                            awardControl = !awardControl

                            // comment'in ödül bilgisi güncellenir.
                            document.update("comment_award",awardControl)
                                .addOnSuccessListener {
                                    // awardControl'e göre userScore arttırılır.
                                    updateUserScore(commentUserId?:"", awardControl) { state ->
                                        when(state) {
                                            is UiState.Loading -> {}
                                            is UiState.Success -> {
                                                result.invoke(UiState.Success(awardControl))
                                            }
                                            is UiState.Failure -> {
                                                result.invoke(
                                                    UiState.Failure(state.error)
                                                )
                                            }
                                        }
                                    }
                                }
                                .addOnFailureListener {message->
                                    result.invoke(
                                        UiState.Failure(message.localizedMessage)
                                    )
                                }
                        }
                    } else {
                        result.invoke(
                            UiState.Failure("comment not found")
                        )
                    }
                }
                .addOnFailureListener { error ->
                    result.invoke(
                        UiState.Failure(error.localizedMessage)
                    )
                }
        }
    }

    override fun updateUserScore(userId: String, control: Boolean, result: (UiState<String>) -> Unit) {
        // İlk önce user verisi çekilir ve userScore alınır.
        // parametre olarak alınan boolean'a göre userScore arttırılır veya azaltılır
        val document = database.collection(FirestoreCollection.USER).document(userId)
        document.get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                var userScore = user?.user_score
                if(control) {
                    if(userScore != null) {
                        userScore += 100
                    }
                } else {
                    if(userScore != null) {
                        userScore -= 100
                    }
                }
                document.update("user_score", userScore)
                    .addOnSuccessListener {
                        result.invoke(UiState.Success("UserScore has been increased."))
                    }
                    .addOnFailureListener {
                        result.invoke(UiState.Success("UserScore has been reduced."))
                    }
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun increaseCommentCount(postId: String, result: (UiState<String>) -> Unit) {
        //  herhangi bir kullanıcı comment attığı zaman ilgili postun "comment_count"u bir arttırılır.
        val document = database.collection(FirestoreCollection.POST_FEED).document(postId)
        document.get()
            .addOnSuccessListener {
                val post = it.toObject(PostFeed::class.java)
                val postCommentCount = post!!.post_comment_count!!.plus(1)
                document.update("post_comment_count",postCommentCount)
                    .addOnSuccessListener {
                        result.invoke(
                            UiState.Success("Comment count has been increased.")
                        )
                    }
                    .addOnFailureListener {error->
                        result.invoke(
                            UiState.Failure(error.localizedMessage)
                        )
                    }
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun getComments(postId: String, result: (UiState<List<Comment>>) -> Unit) {
        // PostFeed'e bağlı ilgili comment'ler çekilir.
        database.collection(FirestoreCollection.COMMENT).whereEqualTo("comment_rootpost_id",postId).get()
            .addOnSuccessListener {
                val commentList = arrayListOf<Comment>()
                for(document in it) {
                    val comment = document.toObject(Comment::class.java)
                    commentList.add(comment)
                }
                result.invoke(
                    UiState.Success(commentList)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(it.localizedMessage)
                )
            }
    }
}