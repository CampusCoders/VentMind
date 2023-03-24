package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.Trend
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.util.FirestoreCollection
import com.campuscoders.ventmind.util.UiState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ListRepositoryImp(
    val database: FirebaseFirestore
): ListRepository {
    override fun getUsers(result: (UiState<List<User>>) -> Unit) {
        database.collection(FirestoreCollection.USER).orderBy("user_score",Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                val userList = arrayListOf<User>()
                for(document in it){
                    val user = document.toObject(User::class.java)
                    userList.add(user)
                }
                result.invoke(UiState.Success(userList!!))
            }
            .addOnFailureListener{
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun getTrends(result: (UiState<List<Trend>>) -> Unit) {
        database.collection(FirestoreCollection.TREND).orderBy("trend_count",Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                val trendList = arrayListOf<Trend>()
                for(document in it){
                    val trend = document.toObject(Trend::class.java)
                    trendList.add(trend)
                }
                result.invoke(UiState.Success(trendList!!))
            }
            .addOnFailureListener{
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }
}