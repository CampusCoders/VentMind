package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.PostFeed
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.util.UiState
import com.google.firebase.firestore.FirebaseFirestore

class ProfileRepositoryImp(
    val database: FirebaseFirestore
): ProfileRepository {
    override fun getUser(result: (UiState<User>) -> Unit) {
        // kullanıcının User'dan verisi çekilir.
    }

    override fun getUserPosts(result: (UiState<List<PostFeed>>) -> Unit) {
        // kullanıcı id'sine göre PostFeed'den veriler çekilir. (kullanıcının kendi postları)
    }

    override fun setUserBio(bio: String, result: (UiState<String>) -> Unit) {
        // kullanıcı id'sine göre User'daki bio güncellenir.
    }
}