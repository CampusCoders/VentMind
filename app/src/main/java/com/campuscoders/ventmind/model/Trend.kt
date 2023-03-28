package com.campuscoders.ventmind.model

data class Trend(
    var trend_count: Int? = 0,
    var trend_name: String? = "",
    var trend_avatar: String? = "",
    var trend_direction: Boolean? = false
)