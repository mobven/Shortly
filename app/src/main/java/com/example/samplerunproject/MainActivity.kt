package com.example.samplerunproject

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.samplerunproject.api.ApiClient
import com.example.samplerunproject.base.BaseActivity
import com.example.samplerunproject.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback

@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {

    val shortenItButton = findViewById<MaterialButton>(R.id.shorten_it_button)
    val linkText = findViewById<TextInputEditText>(R.id.shorten_link_edt)

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        shortenItButton.setOnClickListener {
            callShortLink(linkText.text.toString())
        }

    }

    fun callShortLink(editLink:String) {
        ApiClient.getApiService().getLinks(editLink).enqueue(object :
            Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                Log.d("deneme", "${response.body()}")
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Log.d("deneme", "${t.message}")
            }
        })
    }
}