package com.example.samplerunproject

data class Result(
    val code: String,
    val full_share_link: String = "",
    val full_short_link: String,
    val full_short_link2: String,
    val original_link: String,
    val share_link: String,
    val short_link: String,
    val short_link2: String
)