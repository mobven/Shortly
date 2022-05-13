package com.mobven.shortly

data class Response(
    val ok: Boolean,
    val result: ShortenData
)

data class Error(
    val error: String
)