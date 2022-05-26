package com.mobven.shortly.api

import com.mobven.shortly.BaseResponse
import com.mobven.shortly.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("shorten")
    suspend fun getLinks(@Query("url") url: String?): Response
}