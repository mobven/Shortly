package com.example.samplerunproject

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.samplerunproject.adapter.ShortLinkAdapter
import com.example.samplerunproject.api.ApiClient
import com.example.samplerunproject.base.BaseActivity
import com.example.samplerunproject.databinding.ActivityMainBinding
import com.example.samplerunproject.room.LinkDao
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import java.net.SocketTimeoutException

@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {

    private val shortLinkAdapter = ShortLinkAdapter()
    lateinit var groupMain: Group
    lateinit var tvHistory: TextView
    private lateinit var linkDao: LinkDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainBinding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)

        mainBinding.lifecycleOwner = this




        val shortenItButton = findViewById<MaterialButton>(R.id.shorten_it_button)
        val linkText = findViewById<TextInputEditText>(R.id.shorten_link_edt)
        val rvLinks = findViewById<RecyclerView>(R.id.rv_links)
        groupMain = findViewById(R.id.group_main)
        tvHistory = findViewById(R.id.tv_history)

       // val db = Room.databaseBuilder(applicationContext, LinkListDatabase::class.java, "linkList").fallbackToDestructiveMigration().allowMainThreadQueries().build()
       val db = LinkListDatabase.getDatabase(this@MainActivity)
        linkDao = db.listDAO()


        shortenItButton.setOnClickListener {
            if (linkText.text.toString().isBlank()) {
                checkEditLink(linkText, true)
            } else {
                checkEditLink(linkText, false)
                callShortLink(linkText.text.toString(),mainBinding.progresBar)
            }
        }

        rvLinks.adapter = shortLinkAdapter

        shortLinkAdapter.itemRemoveListener =  {
            linkDao.deleteLink(it)
        }

        linkDao.getLinkList().observe(this) {
            shortLinkAdapter.submitList(it)
            //mainBinding.toolbar.visibility = View.VISIBLE
            groupMain.visibility = View.GONE
            tvHistory.visibility = View.VISIBLE
        }


    }

    private fun checkEditLink(textInput: TextInputEditText, isValid: Boolean) {
        with(textInput) {
            if (isValid) {
                setHintTextColor(ContextCompat.getColor(this@MainActivity, R.color.red))
                hint = getString(R.string.error_link)
                setBackgroundResource(R.drawable.bg_edittext_error)
            } else {
                setHintTextColor(ContextCompat.getColor(this@MainActivity, R.color.gray))
                hint = getString(R.string.hint_link)
                setBackgroundResource(R.drawable.bg_edittext)
            }
        }
    }

    private fun callShortLink(editLink:String,pb: ProgressBar) {
        pb.visibility = View.VISIBLE
        ApiClient.getApiService().getLinks(editLink).enqueue(object :
            Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                pb.visibility = View.GONE
                //Log.d("deneme", "${response.body()}")
                val result = response.body()?.result
                result?.let {
                    linkDao.insertLink(it)
                }?: run {
                    val gson = Gson()
                    val error = gson.fromJson(response.errorBody()?.string(), Error::class.java)
                    Toast.makeText(this@MainActivity, error.error, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                pb.visibility = View.GONE
                when(t) {
                    is SocketTimeoutException -> {
                        val alertDialog = AlertDialog.Builder(this@MainActivity)
                            .setTitle("ERROR")
                            .setMessage("An error occurred")
                            .setPositiveButton("Retry"
                            ) { dialog, which ->
                                //retry
                            }
                            .setNegativeButton("Cancel"
                            ) { dialog, which ->
                                //cancel
                            }.create()
                        alertDialog.show()
                    }
                    else -> {

                    }
                }
                Log.d("deneme", "${t.message}")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return true
    }
}