package com.campuscoders.ventmind.model

data class Comment(
    val comment_avatar : String? = "",
    val comment_award : Int? = 0,
    val comment_content : String? = "",
    val comment_rootpost_id : String? = "",
    val comment_user_id : String? = "",
    val comment_user_nick : String? = ""
)