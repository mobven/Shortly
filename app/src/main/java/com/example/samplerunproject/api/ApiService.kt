package com.example.samplerunproject.api

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("shorten")
    fun getLinks(@Query("url") url : String?)
}