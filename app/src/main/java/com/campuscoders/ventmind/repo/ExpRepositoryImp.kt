package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.PostExp
import com.campuscoders.ventmind.model.PostFeed
import com.campuscoders.ventmind.util.FirestoreCollection
import com.campuscoders.ventmind.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ExpRepositoryImp(
    val database: FirebaseFirestore,
    val auth: FirebaseAuth
): ExpRepository {
    override fun getPosts(result: (UiState<List<PostExp>>) -> Unit) {
        database.collection(FirestoreCollection.POST_EXP).get()
            .addOnSuccessListener {
                val postExpList = arrayListOf<PostExp>()
                for (document in it){
                    val post = document.toObject(PostExp::class.java)
                    postExpList.add(post)
                }
                result.invoke(UiState.Success(postExpList))
            }
            .addOnFailureListener{
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun getPosts(feeling: String, result: (UiState<List<PostExp>>) -> Unit) {
        database.collection(FirestoreCollection.POST_EXP).whereEqualTo("post_tag",feeling).get()
            .addOnSuccessListener {
                val postExpList = arrayListOf<PostExp>()
                for (document in it){
                    val postExp = document.toObject(PostExp::class.java)
                    postExpList.add(postExp)
                }
                result.invoke(UiState.Success(postExpList!!))
            }
            .addOnFailureListener{
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun checkLike(postId: String, result: (UiState<Boolean>) -> Unit){
        // kullanıcı id'si ile alınan postId birleştirilip LikeExp'de arama yapılır. Sonuca göre boolean ifade döndürür.
        val userId = auth.currentUser!!.uid
        val userLikePostId = userId+postId

    }

    override fun updatePostExpLike(postId: String, result: (UiState<String>) -> Unit) {
        // checkLike'ın boolean sonucuna göre PostExp'te "post_like_count" değeri arttırılır veya azaltılır.

    }

    override fun checkDislike(postId: String, result: (UiState<Boolean>) -> Unit) {
        // kullanıcı id'si ile alınan post id birleştirilip Dislike'da arama yapılır. sonuç boolean döner.
    }

    override fun updatePostExpDislike(postId: String, result: (UiState<String>) -> Unit) {
        // checkDislike sonucuna göre PostExp'de "post_dislike_count" değeri arttırılır veya azaltılır.
    }
}