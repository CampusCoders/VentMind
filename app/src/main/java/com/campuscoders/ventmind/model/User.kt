package com.campuscoders.ventmind.model

data class User(
    var user_avatar: String? = "",
    var user_bio: String? = "",
    var user_email: String? = "",
    var user_lock: Boolean? = true,
    var user_nick: String? = "",
    var user_score: Int? = 0,
    var user_id: String? = ""
)