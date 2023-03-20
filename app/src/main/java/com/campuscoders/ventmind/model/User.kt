package com.campuscoders.ventmind.model

data class User(
    var user_avatar: String,
    var user_bio: String,
    var user_email: String,
    var user_lock: Boolean,
    var user_nick: String,
    var user_score: Int
)