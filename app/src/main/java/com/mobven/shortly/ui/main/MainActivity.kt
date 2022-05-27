package com.mobven.shortly.ui.main

import android.R.attr.label
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.mobven.shortly.R
import com.mobven.shortly.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        mainBinding.lifecycleOwner = this

        viewModel.getShortLinkData()

        mainBinding.apply {
            shortenItButton.setOnClickListener {
                if (shortenLinkEdt.text.toString().isBlank()) {
                    checkEditLink( true)
                } else {
                    checkEditLink( false)
                    callShortLink(
                        shortenLinkEdt.text.toString()
                    )
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    val navController = findNavController(mainBinding.fragmentNavHost.id)
                    val graphInflater = navController.navInflater
                    val navListGraph = graphInflater.inflate(R.navigation.nav_list)
                    val navMainGraph = graphInflater.inflate(R.navigation.nav_main)
                    if (it.isNotEmpty()) {
                        navController.graph = navListGraph
                    } else {
                        navController.graph = navMainGraph
                    }
                }
            }
        }
        observeLocalList()
        observeResponse()
        observeError()
    }

    private fun observeLocalList() {
        viewModel.linkListLiveData.observe(this){
        }
    }

    private fun observeError() {
        viewModel.shortenLinkErrorLiveData.observe(this) {
            mainBinding.progresBar.visibility = View.GONE
            showAlertDialog()
        }
    }

    private fun observeResponse() {
        viewModel.shortenLinkLiveData.observe(this){
            mainBinding.progresBar.visibility = View.GONE
            viewModel.insertLink(it.result)
            val clipboard: ClipboardManager =
                getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied", it.result.short_link)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Copied - ${it.result.short_link}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkEditLink(isValid: Boolean) {
        with(mainBinding.shortenLinkEdt) {
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

    private fun callShortLink(editLink: String) {
        mainBinding.progresBar.visibility = View.VISIBLE
        viewModel.shortenLink(mainBinding.shortenLinkEdt.text.toString())
    }

    private fun showAlertDialog(
    ) {
        val alertDialog = AlertDialog.Builder(this@MainActivity)
            .setTitle("ERROR")
            .setMessage("An error occurred")
            .setPositiveButton(
                "Retry"
            ) { dialog, which ->
                callShortLink(mainBinding.shortenLinkEdt.toString())
            }
            .setNegativeButton(
                "Cancel"
            ) { dialog, which ->
                //cancel
            }.create()
        alertDialog.show()
    }
}