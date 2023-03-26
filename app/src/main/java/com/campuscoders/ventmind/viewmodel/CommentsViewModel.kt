package com.campuscoders.ventmind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.campuscoders.ventmind.model.Comment
import com.campuscoders.ventmind.model.PostFeed
import com.campuscoders.ventmind.repo.CommentsRepository
import com.campuscoders.ventmind.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val repository : CommentsRepository
): ViewModel() {

    private val _setComment = MutableLiveData<UiState<String>>()
    val comment : LiveData<UiState<String>>
        get() = _setComment

    private val _rootPost = MutableLiveData<UiState<PostFeed>>()
    val rootPost : LiveData<UiState<PostFeed>>
        get() = _rootPost

    private val _award = MutableLiveData<UiState<Boolean>>()
    val award : LiveData<UiState<Boolean>>
        get() = _award

    private val _updateCommentCount = MutableLiveData<UiState<String>>()
    val updateCommentCount : LiveData<UiState<String>>
        get() = _updateCommentCount

    private val _comments = MutableLiveData<UiState<List<Comment>>>()
    val comments: LiveData<UiState<List<Comment>>>
        get() = _comments

    fun setCommentFun(comment: Comment, rootPostId: String){
        _setComment.value = UiState.Loading
        repository.setComment(comment, rootPostId){
            _setComment.value = it
        }
    }

    fun getRootPostFun(postId: String){
        _rootPost.value = UiState.Loading
        repository.getRootPost(postId){
            _rootPost.value = it
        }
    }

    fun giveAwardFun(commentId: String, postId:String){
        _award.value = UiState.Loading
        repository.giveAward(commentId,postId){
            _award.value = it
        }
    }

    fun updateCommentCountFun(postId: String){
        _updateCommentCount.value = UiState.Loading
        repository.increaseCommentCount(postId){
            _updateCommentCount.value = it
        }
    }

    fun getCommentsFun(postId: String) {
        _comments.value = UiState.Loading
        repository.getComments(postId) {
            _comments.value = it
        }
    }
}