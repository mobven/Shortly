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
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobven.shortly.R
import com.mobven.shortly.adapter.ShortLinkPagingAdapter
import com.mobven.shortly.analytics.AnalyticsManager
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

    @Inject
    lateinit var shortLinkPagingAdapter: ShortLinkPagingAdapter

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var analyticsManager: AnalyticsManager

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
        with(binding) {
            rvLinks.apply {
                adapter = shortLinkPagingAdapter
                setHasFixedSize(true)
                addItemDecoration(
                    SpaceItemDecoration(
                        resources.getDimensionPixelSize(R.dimen._12sdp),
                        false
                    )
                )
            }
            etSearch.doAfterTextChanged {
                viewModel.getLocalShortenLink(it.toString())
            }
        }

        shortLinkPagingAdapter.copyClickListener = { shortenData, position ->
            analyticsManager.copyClickEvent(position)
            viewModel.selectedShortenData(true, shortenData.code)
            shortLinkPagingAdapter.copiedItem = shortenData.code
            val clip = ClipData.newPlainText(getString(R.string.copied), shortenData.short_link)
            clipBoardManager.setPrimaryClip(clip)
        }

        shortLinkPagingAdapter.itemShareListener = { item, position ->
            analyticsManager.shareClickEvent(position)
            requireContext().share(item.short_link, getString(R.string.share))
        }

        shortLinkPagingAdapter.itemRemoveListener = { code, shortLink, position ->
            if (clipBoardManager.primaryClip?.getItemAt(0)?.text?.toString()
                    .equals(shortLink) && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            ) {
                clipBoardManager.clearPrimaryClip()
            }
            analyticsManager.deleteClickEvent(position)
            viewModel.delete(code)
        }

        shortLinkPagingAdapter.openUrl = {
            Intent(Intent.ACTION_VIEW, Uri.parse(it)).apply { startActivity(this) }
        }

        shortLinkPagingAdapter.addOnPagesUpdatedListener {
            analyticsManager.linkHistoryScreenEvent(shortLinkPagingAdapter.itemCount)
        }

        shortLinkPagingAdapter.itemFavoriteListener = { isFavorite, code ->
            viewModel.setFavorite(isFavorite, code)
        }
    }

    private fun renderView(uiState: MyLinksUiState) = with(binding) {
        shortLinkPagingAdapter.submitData(lifecycle, uiState.dataList)
    }


    private fun handleEvent(uiEvent: MyLinksUiEvent) = with(binding) {
        when (uiEvent) {
            is MyLinksUiEvent.ShowError -> {
                toast?.cancel()
                toast =
                    Toast.makeText(context, getString(R.string.delete_error), Toast.LENGTH_SHORT)
                toast?.show()
            }
        }
    }

}