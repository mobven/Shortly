package com.example.samplerunproject

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.samplerunproject.adapter.ShortLinkAdapter
import com.example.samplerunproject.base.BaseActivity
import com.example.samplerunproject.databinding.ActivityMainBinding
import com.example.samplerunproject.room.LinkDao
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {

    private val shortLinkAdapter = ShortLinkAdapter()
    lateinit var groupMain: Group
    lateinit var tvHistory: TextView
    private lateinit var linkDao: LinkDao
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var textInput: TextInputEditText
    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

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
                callShortLink(linkText.text.toString(),linkText)
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
        observeResponse()
    }

    private fun observeShortData(){
        viewModel.shortLiveData.observe(this){
            shortLinkAdapter.submitList(it)
            groupMain.visibility = View.GONE
            tvHistory.visibility = View.VISIBLE
        }
    }

    private fun observeResponse(){
        viewModel.result.observe(this){
            mainBinding.progresBar.visibility = View.GONE
            GlobalScope.launch {
                linkDao.insertLink(it)
            }
        }

        viewModel.error.observe(this){
            mainBinding.progresBar.visibility = View.GONE
            showAlertDialog(this@MainActivity, textInput)
        }

        viewModel.toast.observe(this){
            mainBinding.progresBar.visibility = View.GONE
            Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
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

    private fun callShortLink(editLink: String, textInput: TextInputEditText) {
        mainBinding.progresBar.visibility = View.VISIBLE
        this.textInput = textInput
        viewModel.callResponse(editLink)
    }
    private fun showAlertDialog(ctx : Context,linkText:TextInputEditText){
        val alertDialog = AlertDialog.Builder(this@MainActivity)
            .setTitle("ERROR")
            .setMessage("An error occurred")
            .setPositiveButton(
                "Retry"
            ) { dialog, which ->
                callShortLink(linkText.text.toString(),linkText)
            }
            .setNegativeButton(
                "Cancel"
            ) { dialog, which ->
                //cancel
            }.create()
        alertDialog.show()
    }
}