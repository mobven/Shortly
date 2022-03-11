package com.example.samplerunproject

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Result(
    val code: String,
    val full_share_link: String = "",
    val full_short_link: String,
    val full_short_link2: String,
    val original_link: String,
    val share_link: String,
    val short_link: String,
    val short_link2: String
){
    @PrimaryKey(autoGenerate = true) var uid: Int = 0
}