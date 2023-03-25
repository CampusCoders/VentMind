package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.Comment
import com.campuscoders.ventmind.model.PostFeed
import com.campuscoders.ventmind.util.FirestoreCollection
import com.campuscoders.ventmind.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CommentsRepositoryImp(
    val database: FirebaseFirestore,
    val auth: FirebaseAuth,
    ): CommentsRepository {

    override fun setComment(comment: Comment, result: (UiState<String>) -> Unit) {
        // Comment'e yeni bir document eklenir
        database.collection(FirestoreCollection.COMMENT).add(comment)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    result.invoke(UiState.Success("Post has been added"))
                }else{
                    result.invoke(UiState.Failure("Post add operation failed"))
                }
            }
            .addOnFailureListener{
                result.invoke(UiState.Failure("Post add operation failed, check authentication"))
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

    override fun checkOwnPost(postUserId: String, result: (UiState<Boolean>) -> Unit) {
        // "post_user_id" ile auth.currentUser karşılaştırılır ve sonuç boolean döner.
        // kullanıcı kendi postuna mı bakıyor kontrol edilir.
        // eğer true(kullanıcı kendi postuna bakıyor) ise; comment'lerin sol köşelerinde boş ödül ikonları gözükür.
        if(postUserId.equals(auth.currentUser!!.uid)){
            result.invoke(UiState.Success(true))
        }else{
            result.invoke(UiState.Success(false))
        }
    }

    override fun giveAward(commentId: String,postId: String, result: (UiState<Boolean>) -> Unit) {
        this.checkOwnPost(postId){
            when(it){
                is UiState.Loading -> {}
                is UiState.Success -> {
                    if(it.data){
                        database.collection(FirestoreCollection.COMMENT).document(commentId)
                            .update("comment_award",true)
                            .addOnSuccessListener {
                                result.invoke(UiState.Success(true))  //comment_award , true olarak güncellendi
                            }
                            .addOnFailureListener {
                                result.invoke(UiState.Success(false))
                            }
                    }else{
                        result.invoke(UiState.Success(false))
                    }
                }
                is UiState.Failure ->{}
            }
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
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }
}