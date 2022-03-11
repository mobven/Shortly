package com.example.samplerunproject

data class Response(
    val ok: Boolean,
    val result: Result
)

data class Error(
    val error: String
)