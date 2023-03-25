package com.campuscoders.ventmind.repo

import com.campuscoders.ventmind.model.Dislike
import com.campuscoders.ventmind.model.LikeExp
import com.campuscoders.ventmind.model.PostExp
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
        val userId = auth.currentUser?.uid
        val likeExpId = userId + postId

        val document = database.collection(FirestoreCollection.LIKE_EXP).document(likeExpId)
        document.get()
            .addOnSuccessListener {
                if(it.exists()) {

                    // like atılmış
                    // like kaydını siliyoruz ve "true" yollayıp like atıldı bilgisini result ile veriyoruz.
                    document.delete()
                        .addOnCompleteListener {
                            result.invoke(UiState.Success(true))
                        }
                        .addOnFailureListener {
                            result.invoke(UiState.Failure("like exp silme işlemi başarısız."))
                        }
                } else {

                    // like atılmamış
                    // yeni bir likeExp kaydı oluşturuyoruz ve "false" yollayıp like atılmadı bilgisini result ile veriyoruz
                    val likeExp = LikeExp(postId,userId)
                    document.set(likeExp)
                        .addOnCompleteListener{
                            result.invoke(UiState.Success(false))
                        }
                        .addOnFailureListener{
                            result.invoke(UiState.Failure("Adding FeedExp error"))
                        }
                }
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun updateLikeCount(postId: String, result: (UiState<Boolean>) -> Unit) {
        // checkLike'ın boolean sonucuna göre PostExp'te "post_like_count" değeri arttırılır veya azaltılır.
        val document = database.collection(FirestoreCollection.POST_EXP).document(postId)
        document.get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val postExp = it.toObject(PostExp::class.java)
                    var likeCount = postExp?.post_like_count

                    // checkLike'a göre like arttırma veya azaltma işlemleri
                    checkLike(postId) { state ->
                        when(state) {
                            is UiState.Loading -> {}
                            is UiState.Success -> {
                                if(state.data) { // like atılmış
                                    // Burada like azaltma işlemi yapılacak ve PostExp'in like_count'u güncellenecek
                                    if (likeCount != null) { likeCount-- }
                                    document.update("post_like_count",likeCount)
                                        .addOnCompleteListener {
                                            result.invoke(UiState.Success(false))
                                        }
                                        .addOnFailureListener {
                                            result.invoke(UiState.Failure("Like count process error"))
                                        }

                                } else {   // like atılmamış
                                    // Burada like arttırma işlemi yapılacak ve PostExp'in like_count'u güncellenecek
                                    if(likeCount != null) { likeCount++ }
                                    document.update("post_like_count", likeCount)
                                        .addOnCompleteListener {
                                            result.invoke(UiState.Success(true))
                                        }
                                        .addOnFailureListener {
                                            result.invoke(UiState.Failure("Like count process error"))
                                        }
                                }
                            }
                            is UiState.Failure -> {
                                result.invoke(UiState.Failure(state.error))
                            }
                        }
                    }
                } else {
                    result.invoke(UiState.Failure("ERROR!!!"))
                }
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun checkDislike(postId: String, result: (UiState<Boolean>) -> Unit) {
        // kullanıcı id'si ile alınan post id birleştirilip Dislike'da arama yapılır. sonuç boolean döner.
        val userId = auth.currentUser?.uid
        val dislikeExpId = userId + postId

        val document = database.collection(FirestoreCollection.DISLIKE).document(dislikeExpId)
        document.get()
            .addOnSuccessListener {
                if(it.exists()) {

                    // dislike atılmış
                    // dislike kaydını siliyoruz ve "true" yollayıp dislike atıldı bilgisini result ile veriyoruz.
                    document.delete()
                        .addOnCompleteListener {
                            result.invoke(UiState.Success(true))
                        }
                        .addOnFailureListener {
                            result.invoke(UiState.Failure("Dislike silme işlemi başarısız."))
                        }
                } else {

                    // Dislike atılmamış
                    // yeni bir Dislike kaydı oluşturuyoruz ve "false" yollayıp dislike atılmadı bilgisini result ile veriyoruz
                    val dislike = Dislike(postId,userId)
                    document.set(dislike)
                        .addOnCompleteListener{
                            result.invoke(UiState.Success(false))
                        }
                        .addOnFailureListener{
                            result.invoke(UiState.Failure("Adding Dislike error"))
                        }
                }
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun updateDislikeCount(postId: String, result: (UiState<Boolean>) -> Unit) {
        // checkDislike sonucuna göre PostExp'de "post_dislike_count" değeri arttırılır veya azaltılır.
        val document = database.collection(FirestoreCollection.POST_EXP).document(postId)
        document.get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val postExp = it.toObject(PostExp::class.java)
                    var dislikeCount = postExp?.post_dislike_count

                    // checkDislike'a göre dislike arttırma veya azaltma işlemleri
                    checkDislike(postId) { state ->
                        when(state) {
                            is UiState.Loading -> {}
                            is UiState.Success -> {
                                if(state.data) {    // dislike atılmış
                                    // Burada dislike azaltma işlemi yapılacak ve PostExp'in dislike_count'u güncellenecek
                                    if (dislikeCount != null) { dislikeCount-- }
                                    document.update("post_dislike_count",dislikeCount)
                                        .addOnCompleteListener {
                                            result.invoke(UiState.Success(false))
                                        }
                                        .addOnFailureListener {
                                            result.invoke(UiState.Failure("Dislike count process error"))
                                        }

                                } else {   // dislike atılmamış
                                    // Burada dislike arttırma işlemi yapılacak ve PostExp'in dislike_count'u güncellenecek
                                    if(dislikeCount != null) { dislikeCount++ }
                                    document.update("post_dislike_count", dislikeCount)
                                        .addOnCompleteListener {
                                            result.invoke(UiState.Success(true))
                                        }
                                        .addOnFailureListener {
                                            result.invoke(UiState.Failure("Dislike count process error"))
                                        }
                                }
                            }
                            is UiState.Failure -> {
                                result.invoke(UiState.Failure(state.error))
                            }
                        }
                    }
                } else {
                    result.invoke(UiState.Failure("ERROR!!!"))
                }
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }
}