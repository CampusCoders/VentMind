package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.PostExp
import com.campuscoders.ventmind.util.UiState
import com.google.firebase.firestore.FirebaseFirestore

class ExpRepositoryImp(
    val database: FirebaseFirestore
): ExpRepository {
    override fun getPosts(resul: (UiState<List<PostExp>>) -> Unit) {
        // PostExp'den filtresiz bir şekilde tüm postlar çekilir.
    }

    override fun getPosts(feeling: String, result: (UiState<List<PostExp>>) -> Unit) {
        // PostExp'den "post_tag"e göre filtrelenmiş veriler çekilir
    }

    override fun checkLike(postId: String, result: (UiState<Boolean>) -> Unit) {
        // kullanıcı id'si ile alınan postId birleştirilip LikeExp'de arama yapılır. Sonuca göre boolean ifade döndürür.
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