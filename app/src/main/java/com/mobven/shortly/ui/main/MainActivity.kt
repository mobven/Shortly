package com.mobven.shortly.ui.main

import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.test.espresso.IdlingResource
import com.mobven.shortly.R
import com.mobven.shortly.SimpleIdlingResource
import com.mobven.shortly.analytics.AnalyticsManagerImpl
import com.mobven.shortly.databinding.ActivityMainBinding
import com.mobven.shortly.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), TextView.OnEditorActionListener {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var mainBinding: ActivityMainBinding

    private var mIdlingResource: SimpleIdlingResource? = null

    @Inject
    lateinit var analyticsManagerImpl: AnalyticsManagerImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainBinding.lifecycleOwner = this
        mainBinding.apply {
            shortenItButton.setOnClickListener {
                viewModel.buttonClicked(mainBinding.shortenLinkEdt.text.toString().isBlank())
            }
            shortenLinkEdt.setOnEditorActionListener(this@MainActivity)
        }
        checkTheme()
        val navController = (supportFragmentManager.findFragmentById(R.id.fragment_nav_host) as NavHostFragment).navController
        val graphInflater = navController.navInflater
        val navListGraph = graphInflater.inflate(R.navigation.nav_list)
        val navMainGraph = graphInflater.inflate(R.navigation.nav_main)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when(it) {
                        is ShortlyUiState.Loading -> {
                            mainBinding.progressBar.visibility = View.VISIBLE
                        }
                        is ShortlyUiState.Empty -> {
                            analyticsManagerImpl.getStartedScreenEvent()
                            navController.graph = navMainGraph
                        }
                        is ShortlyUiState.Success -> {
                            navController.graph = navListGraph
                            mainBinding.progressBar.visibility = View.GONE
                        }
                        is ShortlyUiState.Error -> {
                            mainBinding.progressBar.visibility = View.GONE
                            mIdlingResource?.setIdleState(true)
                            showAlertDialog()
                        }
                        is ShortlyUiState.LinkShorten -> {
                            viewModel.insertLink(it.data)
                            mainBinding.shortenLinkEdt.text?.clear()
                            mainBinding.progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }
        viewModel.isBlank.observe(this){
            with(mainBinding.shortenLinkEdt) {
                if (it) {
                    setHintTextColor(ContextCompat.getColor(this@MainActivity, R.color.red))
                    hint = getString(R.string.error_link)
                    setBackgroundResource(R.drawable.bg_edittext_error)
                } else {
                    setHintTextColor(ContextCompat.getColor(this@MainActivity, R.color.gray))
                    hint = getString(R.string.hint_link)
                    setBackgroundResource(R.drawable.bg_edittext)
                    mIdlingResource?.setIdleState(false)
                    callShortLink(
                        mainBinding.shortenLinkEdt.text.toString()
                    )
                }
            }
        }
        handleIntent(intent)
    }

    private fun checkTheme() {
       val isDarkTheme = this.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        analyticsManagerImpl
            .themeTypeAppEvent(if (isDarkTheme) Constants.AnalyticsEvent.DARK_MODE else Constants.AnalyticsEvent.LIGHT_MODE)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent : Intent?){
        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain"){
            mainBinding.shortenLinkEdt.setText(intent.getStringExtra(Intent.EXTRA_TEXT))
        }
    }

    private fun callShortLink(editLink: String) {
        mainBinding.progressBar.visibility = View.VISIBLE
        viewModel.shortenLink(editLink)
    }

    private fun showAlertDialog(
    ) {
        val alertDialog = AlertDialog.Builder(this@MainActivity)
            .setTitle("ERROR")
            .setMessage("An error occurred")
            .setPositiveButton(
                "Retry"
            ) { _, _ ->
                callShortLink(mainBinding.shortenLinkEdt.text.toString())
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
            viewModel.buttonClicked(mainBinding.shortenLinkEdt.text.toString().isBlank())
        }
        return false
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