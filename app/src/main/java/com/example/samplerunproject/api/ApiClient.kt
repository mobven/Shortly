package com.example.samplerunproject.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    fun getApiService(): ApiService {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://api.shrtco.de/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofitBuilder.create(ApiService::class.java)
    }
}