package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.Avatar
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.util.FirestoreCollection
import com.campuscoders.ventmind.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AvatarsRepositoryImp(
    val database: FirebaseFirestore,
    val auth: FirebaseAuth
    ): AvatarsRepository {

    override fun getAvatars(result: (UiState<List<Avatar>>) -> Unit) {
        database.collection(FirestoreCollection.AVATAR).get()
            .addOnSuccessListener {
                val avatarList = arrayListOf<Avatar>()
                for(document in it){
                    val avatar = document.toObject(Avatar::class.java)
                    avatarList.add(avatar)
                }
                result.invoke(UiState.Success(avatarList))
            }
            .addOnFailureListener{
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun getUserScore(result: (UiState<Int>) -> Unit) {
        val document = database.collection(FirestoreCollection.USER).document(
            auth.currentUser!!.uid ?:"error")

        document.get()
            .addOnSuccessListener {
                if (it.exists()){
                    val user = it.toObject(User::class.java)
                    val user_score = user!!.user_score
                    result.invoke(UiState.Success(user_score!!))
                }else{
                    result.invoke(UiState.Failure("UserScore field not found"))
                }
            }
            .addOnFailureListener{
                result.invoke(UiState.Failure("Authentication failed"))
            }
    }
}