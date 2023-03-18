package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.PostExp
import com.campuscoders.ventmind.model.PostFeed
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.util.UiState
import com.google.firebase.firestore.FirebaseFirestore

class CreatePostRepositoryImp(
    val database: FirebaseFirestore
): CreatePostRepository {

    override fun getUser(result: (UiState<User>) -> Unit) {
        // User'dan kullanıcının verileri çekilir.
    }

    override fun addPostFeed(post: PostFeed, result: (UiState<String>) -> Unit) {
        // PostFeed'e yeni bir document ekle
    }

    override fun addPostExp(post: PostExp, result: (UiState<String>) -> Unit) {
        // PostExp'e yeni bir document ekler
    }
}