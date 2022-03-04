package com.example.samplerunproject

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.samplerunproject.adapter.ShortLinkAdapter
import com.example.samplerunproject.api.ApiClient
import com.example.samplerunproject.base.BaseActivity
import com.example.samplerunproject.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import java.net.SocketTimeoutException

@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {

    private val shortLinkAdapter = ShortLinkAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val shortenItButton = findViewById<MaterialButton>(R.id.shorten_it_button)
        val linkText = findViewById<TextInputEditText>(R.id.shorten_link_edt)
        val rvLinks = findViewById<RecyclerView>(R.id.rv_links)

        shortenItButton.setOnClickListener {
            callShortLink(linkText.text.toString())
        }

        rvLinks.adapter = shortLinkAdapter
    }

    private fun callShortLink(editLink:String) {
        ApiClient.getApiService().getLinks(editLink).enqueue(object :
            Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                //Log.d("deneme", "${response.body()}")
                var result = response.body()?.result
                result?.let {
                    shortLinkAdapter.setData(mutableListOf(it))
                }

            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                when(t) {
                    is SocketTimeoutException -> {
                        var alertDialog = AlertDialog.Builder(this@MainActivity)
                            .setTitle("ERROR")
                            .setMessage("An error occurred")
                            .setPositiveButton("Retry", object: DialogInterface.OnClickListener{
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    //retry
                                }
                            })
                            .setNegativeButton("Cancel", object: DialogInterface.OnClickListener{
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    //cancel
                                }
                            }).create()
                        alertDialog.show()
                    }
                }
                Log.d("deneme", "${t.message}")
            }
        })
    }
}