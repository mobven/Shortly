package com.mobven.shortly.ui.list

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    private var toast: Toast? = null

    @Inject
    lateinit var clipBoardManager: ClipboardManager

    @Inject
    lateinit var shortLinkAdapter: ShortLinkAdapter

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
        with(binding) {
            rvLinks.layoutManager = linearLayoutManager

            rvLinks.adapter = shortLinkAdapter
        }

        viewModel.apply {
            linkList.observe(viewLifecycleOwner) {
                shortLinkAdapter.setData(it)
            }

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
            deleteError.observe(viewLifecycleOwner) {
                toast?.cancel()
                toast = Toast.makeText(context, "Silerken Bir Hata Olu??tu!", Toast.LENGTH_SHORT)
                toast?.show()

            }
        }
    }

}