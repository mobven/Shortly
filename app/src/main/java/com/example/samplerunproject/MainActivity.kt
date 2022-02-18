package com.example.samplerunproject

import android.os.Bundle
import android.util.Log
import com.example.samplerunproject.api.ApiClient
import com.example.samplerunproject.base.BaseActivity
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ApiClient.getApiService().getLinks("asdsadasdsad").enqueue(object :
            Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                Log.d("deneme", "${response.body()}")
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Log.d("deneme", "${t.message}")
            }
        })

        val shortenItButton = findViewById<MaterialButton>(R.id.shorten_it_button)

    }
}