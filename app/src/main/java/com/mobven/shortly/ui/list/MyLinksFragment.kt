package com.mobven.shortly.ui.list

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobven.shortly.R
import com.mobven.shortly.adapter.ShortLinkAdapter
import com.mobven.shortly.databinding.FragmentMylistBinding
import com.mobven.shortly.utils.SpaceItemDecoration
import com.mobven.shortly.utils.collectEvent
import com.mobven.shortly.utils.collectState
import com.mobven.shortly.utils.share
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MyLinksFragment : Fragment() {
    private lateinit var binding: FragmentMylistBinding
    private val viewModel: MyLinksViewModel by viewModels()
    private var toast: Toast? = null

    @Inject
    lateinit var clipBoardManager: ClipboardManager

    private var shortLinkAdapter = ShortLinkAdapter(emptyList())

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        collectState(viewModel.uiState, ::renderView)
        collectEvent(viewModel.uiEvent, ::handleEvent)
    }

    private fun initView() {
        binding.rvLinks.apply {
            adapter = shortLinkAdapter
            setHasFixedSize(true)
            addItemDecoration(
                SpaceItemDecoration(
                    resources.getDimensionPixelSize(R.dimen._12sdp),
                    false
                )
            )
        }

        shortLinkAdapter.itemClickListener = {
            viewModel.selectedShortenData(true, it.code)
            shortLinkAdapter.copiedItem = it.code
            val clip = ClipData.newPlainText("Copied", it.short_link)
            clipBoardManager.setPrimaryClip(clip)
        }

        shortLinkAdapter.itemShareListener = {
            requireContext().share(it.short_link, "Share")
        }

        shortLinkAdapter.itemRemoveListener = { code, shortLink ->
            if (clipBoardManager.primaryClip?.getItemAt(0)?.text?.toString()
                    .equals(shortLink) && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            ) {
                clipBoardManager.clearPrimaryClip()
            }
            viewModel.delete(code)
        }

        shortLinkAdapter.openUrl = {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(browserIntent)
        }
    }

    private fun renderView(uiState: MyLinksUiState) = with(binding) {
        shortLinkAdapter.setData(uiState.dataList)
    }

    private fun handleEvent(uiEvent: MyLinksUiEvent) = with(binding) {
        when (uiEvent) {
            is MyLinksUiEvent.ShowError -> {
                toast?.cancel()
                toast = Toast.makeText(context, uiEvent.message, Toast.LENGTH_SHORT)
                toast?.show()
            }
        }
    }

}