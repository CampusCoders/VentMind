package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.Avatar
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.util.UiState
import com.google.firebase.firestore.FirebaseFirestore

class AvatarsRepositoryImp(
    val database: FirebaseFirestore
): AvatarsRepository {

    override fun getAvatars(result: (UiState<List<Avatar>>) -> Unit) {
        // Avatar'dan tüm veriler çekilir.
    }

    override fun getUserScore(result: (UiState<User>) -> Unit) {
        // "user_score"a erişmek için kullanıcının User bilgisi çekilir.
    }
}