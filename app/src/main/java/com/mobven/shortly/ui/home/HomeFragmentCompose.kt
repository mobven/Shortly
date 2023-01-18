package com.mobven.shortly.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mobven.shortly.R
import com.mobven.shortly.databinding.FragmentHomeBinding

@Composable
fun HomeFragmentCompose() {
    Surface {
        Text("Hello Compose")
    }
}

class HomeComposeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home, container, false
        ).apply {
            composeView.setContent {
                MaterialTheme {
                    HomeFragmentCompose()
                }
            }
        }
        return binding.composeView
    }
}