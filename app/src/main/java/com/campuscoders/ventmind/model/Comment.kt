package com.campuscoders.ventmind.model

data class Comment(
    var comment_avatar : String? = "",
    var comment_award : Boolean? = false,
    var comment_content : String? = "",
    var comment_rootpost_id : String? = "",
    var comment_user_id : String? = "",
    var comment_user_nick : String? = "",
    var comment_id: String? = ""
)