package com.mobven.shortly

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "links")
data class ShortenData(
    @PrimaryKey
    val code: String,
    val full_share_link: String = "",
    val full_short_link: String,
    val full_short_link2: String,
    val original_link: String,
    val share_link: String,
    val short_link: String,
    val short_link2: String,
    val isSelected: Boolean = false,
    val isFavorite: Boolean = false
)