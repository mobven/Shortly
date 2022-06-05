package com.mobven.shortly.ui.list

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobven.shortly.adapter.ShortLinkAdapter
import com.mobven.shortly.databinding.FragmentMylistBinding
import com.mobven.shortly.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyLinksFragment : Fragment() {
    private lateinit var binding: FragmentMylistBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var clipBoardManager: ClipboardManager

    @Inject
    lateinit var shortLinkAdapter: ShortLinkAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            rvLinks.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvLinks.adapter = shortLinkAdapter
        }

        viewModel.apply {
            linkList.observe(viewLifecycleOwner) {
                shortLinkAdapter.setData(it)
            }

            clipBoardManager =
                activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            shortLinkAdapter.itemClickListener = {
                selectedShortenData(true, it.code)
                shortLinkAdapter.copiedItem = it.code
                val clip = ClipData.newPlainText("Copied", it.short_link)
                clipBoardManager.setPrimaryClip(clip)
            }

            shortLinkAdapter.itemRemoveListener = { code, shortLink ->
                if (clipBoardManager.primaryClip?.getItemAt(0)?.text?.toString().equals(shortLink))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        clipBoardManager.clearPrimaryClip()
                    }
                deleteLink(code)
            }
        }
    }

}