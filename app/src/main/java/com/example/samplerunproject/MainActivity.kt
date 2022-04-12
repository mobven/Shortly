package com.example.samplerunproject

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.samplerunproject.adapter.ShortLinkAdapter
import com.example.samplerunproject.api.ApiService
import com.example.samplerunproject.base.BaseActivity
import com.example.samplerunproject.databinding.ActivityMainBinding
import com.example.samplerunproject.room.LinkDao
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException

@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {

    @Inject
    lateinit var service: ApiService
    private val shortLinkAdapter = ShortLinkAdapter()
    lateinit var groupMain: Group
    lateinit var tvHistory: TextView
    private lateinit var linkDao: LinkDao
    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainBinding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

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
                callShortLink(linkText.text.toString(), mainBinding.progresBar,linkText)
            }
        }

        rvLinks.adapter = shortLinkAdapter

        shortLinkAdapter.itemRemoveListener = {
            //TODO: Inject edilip suspen'e çevirilecek
            GlobalScope.launch {
                linkDao.deleteLink(it)
            }
        }

        viewModel.getShortLinkData()

        observeShortData()
    }

    private fun observeShortData(){
        viewModel.shortLiveData.observe(this){
            shortLinkAdapter.submitList(it)
            if(it.isEmpty()) {
                groupMain.visibility = View.VISIBLE
                tvHistory.visibility = View.GONE
            } else {
                groupMain.visibility = View.GONE
                tvHistory.visibility = View.VISIBLE
            }
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

    private fun callShortLink(editLink: String, pb: ProgressBar,textInput: TextInputEditText) {
        pb.visibility = View.VISIBLE
        service.getLinks(editLink).enqueue(object :
            Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                pb.visibility = View.GONE
                val result = response.body()?.result
                result?.let {
                    GlobalScope.launch {
                        linkDao.insertLink(it)
                        viewModel.getShortLinkData()
                    }
                } ?: run {
                    val gson = Gson()
                    val error = gson.fromJson(response.errorBody()?.string(), Error::class.java)
                    Toast.makeText(this@MainActivity, error.error, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                pb.visibility = View.GONE
                when (t) {
                    is SocketTimeoutException -> {
                        showAlertDialog(this@MainActivity,textInput, pb )
                    }
                    is SSLHandshakeException ->{
                        showAlertDialog(this@MainActivity,textInput, pb )
                    }
                    else -> {

                    }
                }
                Log.d("deneme", "${t.message}")
            }
        })
    }
    private fun showAlertDialog(ctx : Context,linkText:TextInputEditText,progressBar: ProgressBar){
        val alertDialog = AlertDialog.Builder(this@MainActivity)
            .setTitle("ERROR")
            .setMessage("An error occurred")
            .setPositiveButton(
                "Retry"
            ) { dialog, which ->
                callShortLink(linkText.text.toString(), progressBar,linkText)
            }
            .setNegativeButton(
                "Cancel"
            ) { dialog, which ->
                //cancel
            }.create()
        alertDialog.show()
    }
}