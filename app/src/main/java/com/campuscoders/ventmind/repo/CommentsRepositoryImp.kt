package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.Comment
import com.campuscoders.ventmind.model.PostFeed
import com.campuscoders.ventmind.util.UiState
import com.google.firebase.firestore.FirebaseFirestore

class CommentsRepositoryImp(
    val database: FirebaseFirestore
): CommentsRepository {

    override fun setComment(comment: Comment, result: (UiState<String>) -> Unit) {
        // Comment'e yeni bir document eklenir
    }

    override fun getRootPost(postId: String, result: (UiState<PostFeed>) -> Unit) {
        // PostFeed'den postId'ye göre post çekilir. (yorumların bağlı olduğu root post)
    }

    override fun checkOwnPost(postId: String, result: (UiState<Boolean>) -> Unit) {
        // "post_user_id" ile auth.currentUser karşılaştırılır ve sonuç boolean döner.
        // kullanıcı kendi postuna mı bakıyor kontrol edilir.
        // eğer true(kullanıcı kendi postuna bakıyor) ise; comment'lerin sol köşelerinde boş ödül ikonları gözükür.
    }

    override fun giveAward(commentId: String, result: (UiState<Boolean>) -> Unit) {
        // checkOwnPost true dönerse(gönderi sahibi); tıkladığı comment'in id'si alınır ve "comment_award" güncellenir(true: ödül verildi gibi)
    }

    override fun increaseCommentCount(postId: String, result: (UiState<String>) -> Unit) {
        //  herhangi bir kullanıcı comment attığı zaman ilgili postun "comment_count"u bir arttırılır.
    }
}