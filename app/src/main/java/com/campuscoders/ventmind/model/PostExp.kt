package com.campuscoders.ventmind.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class PostExp(
    @ServerTimestamp
    var created_at : Date? = Date(),
    var post_avatar: String? = "",
    var post_comment_count: Int? = 0,
    var post_content: String? = "",
    var post_dislike_count: Int? = 0,
    var post_nick: String? = "",
    var post_tag: String? = "",
    var post_user_id: String? = ""
)