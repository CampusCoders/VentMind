package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.Trend
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.util.UiState
import com.google.firebase.firestore.FirebaseFirestore

class ListRepositoryImp(
    val database: FirebaseFirestore
): ListRepository {
    override fun getUsers(result: (UiState<List<User>>) -> Unit) {
        // "user_score"a göre sıralanmış User listesi döndürülür.
    }

    override fun getTrends(result: (UiState<List<Trend>>) -> Unit) {
        // "trend_count" a göre sıralanmış Trend listesi döndürülür.
    }
}