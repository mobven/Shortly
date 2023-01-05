package com.mobven.shortly.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.test.espresso.IdlingResource
import com.mobven.extension.gone
import com.mobven.shortly.R
import com.mobven.shortly.SimpleIdlingResource
import com.mobven.shortly.databinding.ActivityMainBinding
import com.mobven.shortly.utils.collectEvent
import com.mobven.shortly.utils.collectState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), TextView.OnEditorActionListener {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var mainBinding: ActivityMainBinding

    private var mIdlingResource: SimpleIdlingResource? = null

    private lateinit var navController: NavController

    private val navListGraph by lazy {
        navController.navInflater.inflate(R.navigation.nav_list)
    }
    private val navMainGraph by lazy {
        navController.navInflater.inflate(R.navigation.nav_main)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainBinding.lifecycleOwner = this

        mainBinding.apply {
            shortenItButton.setOnClickListener { shortenButtonClick() }
            shortenLinkEdt.setOnEditorActionListener(this@MainActivity)
        }

        navController =
            (supportFragmentManager.findFragmentById(R.id.fragment_nav_host) as NavHostFragment).navController

        collectState(viewModel.uiState, ::renderView)
        collectEvent(viewModel.uiEvent, ::handleEvent)
    }

    private fun renderView(uiState: MainUiState) = with(mainBinding) {
        progressBar.isVisible = uiState.isLoading
        if (uiState.dataList.isNotEmpty())
            navController.graph = navListGraph
        else
            navController.graph = navMainGraph

    }

    private fun handleEvent(uiEvent: MainUiEvent) = with(mainBinding) {
        when (uiEvent) {
            is MainUiEvent.ShowError -> {
                progressBar.gone()
                mIdlingResource?.setIdleState(true)
                showAlertDialog()
            }
            is MainUiEvent.LinkShorten -> {
                viewModel.insertLink(uiEvent.data)
                shortenLinkEdt.text?.clear()
                progressBar.gone()
            }
        }
    }

    private fun showAlertDialog() {
        val alertDialog = AlertDialog.Builder(this@MainActivity)
            .setTitle("ERROR")
            .setMessage("An error occurred")
            .setPositiveButton(
                "Retry"
            ) { _, _ ->
                viewModel.shortenLink(mainBinding.shortenLinkEdt.text.toString())
            }
            .setNegativeButton(
                "Cancel"
            ) { _, _ ->
                //cancel
            }.create()
        alertDialog.show()
    }

    override fun onEditorAction(p0: TextView?, imeType: Int, p2: KeyEvent?): Boolean {
        if (imeType == EditorInfo.IME_ACTION_DONE) {
            shortenButtonClick()
        }
        return false
    }

    private fun shortenButtonClick() = with(mainBinding.shortenLinkEdt) {
        val textIsBlank = text.toString().isBlank()

        if (textIsBlank) {
            setHintTextColor(
                androidx.core.content.ContextCompat.getColor(
                    this@MainActivity,
                    com.mobven.shortly.R.color.red
                )
            )
            hint = getString(com.mobven.shortly.R.string.error_link)
            setBackgroundResource(com.mobven.shortly.R.drawable.bg_edittext_error)
        } else {
            setHintTextColor(
                androidx.core.content.ContextCompat.getColor(
                    this@MainActivity,
                    com.mobven.shortly.R.color.gray
                )
            )
            hint = getString(com.mobven.shortly.R.string.hint_link)
            setBackgroundResource(com.mobven.shortly.R.drawable.bg_edittext)
            mIdlingResource?.setIdleState(false)
            viewModel.shortenLink(text.toString())
        }
    }

    /**
     * Only called from test, creates and returns a new [SimpleIdlingResource].
     */
    @VisibleForTesting
    fun getIdlingResource(): IdlingResource {
        if (mIdlingResource == null) {
            mIdlingResource = SimpleIdlingResource()
        }
        return mIdlingResource as SimpleIdlingResource
    }
}