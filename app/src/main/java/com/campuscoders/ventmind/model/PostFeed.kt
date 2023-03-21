package com.campuscoders.ventmind.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class PostFeed(
    @ServerTimestamp
    val created_at: Date? = Date(),
    val post_avatar: String? = "",
    val post_comment_count: Int? = 0,
    val post_content: String? = "",
    val post_like_count: Int? = 0,
    val post_nick: String? = "",
    val post_tag: String? = "",
    val post_user_id: String? = ""
)