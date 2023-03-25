package com.campuscoders.ventmind.model

data class User(
    val user_avatar: String? = "",
    val user_bio: String? = "",
    val user_email: String? = "",
    val user_lock: Boolean? = true,
    val user_nick: String? = "",
    val user_score: Int? = 0,
    var user_id: String? = ""
)