package com.mobven.shortly.ui.main

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.mobven.shortly.R
import com.mobven.shortly.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainBinding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        mainBinding.lifecycleOwner = this

        mainBinding.apply {
            shortenItButton.setOnClickListener {
                if (shortenLinkEdt.text.toString().isBlank()) {
                    checkEditLink(shortenLinkEdt, true)
                } else {
                    checkEditLink(shortenLinkEdt, false)
                    callShortLink(
                        shortenLinkEdt.text.toString(),
                        mainBinding.progresBar,
                        shortenLinkEdt
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

    private fun callShortLink(editLink: String, pb: ProgressBar, textInput: TextInputEditText) {
        pb.visibility = View.VISIBLE
    }

    private fun showAlertDialog(
        ctx: Context,
        linkText: TextInputEditText,
        progressBar: ProgressBar
    ) {
        val alertDialog = AlertDialog.Builder(this@MainActivity)
            .setTitle("ERROR")
            .setMessage("An error occurred")
            .setPositiveButton(
                "Retry"
            ) { dialog, which ->
                callShortLink(linkText.text.toString(), progressBar, linkText)
            }
            .setNegativeButton(
                "Cancel"
            ) { dialog, which ->
                //cancel
            }.create()
        alertDialog.show()
    }
}