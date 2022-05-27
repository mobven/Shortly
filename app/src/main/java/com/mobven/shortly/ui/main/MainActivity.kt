package com.mobven.shortly.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
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
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainBinding.lifecycleOwner = this

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

        val navController = (supportFragmentManager.findFragmentById(R.id.fragment_nav_host) as NavHostFragment).navController
        val graphInflater = navController.navInflater
        val navListGraph = graphInflater.inflate(R.navigation.nav_list)
        val navMainGraph = graphInflater.inflate(R.navigation.nav_main)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when(it) {
                        is ShortlyUiState.Loading -> {
                            mainBinding.progresBar.visibility = View.VISIBLE
                        }
                        is ShortlyUiState.Empty -> {
                            navController.graph = navMainGraph
                        }
                        is ShortlyUiState.Success -> {
                            navController.graph = navListGraph
                            mainBinding.progresBar.visibility = View.GONE
                        }
                        is ShortlyUiState.Error -> {
                            mainBinding.progresBar.visibility = View.GONE
                            showAlertDialog()
                        }
                        is ShortlyUiState.LinkShorten -> {
                            viewModel.insertLink(it.data)
                            mainBinding.progresBar.visibility = View.GONE
                        }
                    }
                }
            }
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
        viewModel.shortenLink(editLink)
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