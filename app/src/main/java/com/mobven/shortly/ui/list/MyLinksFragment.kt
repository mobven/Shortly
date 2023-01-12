package com.mobven.shortly.ui.list

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobven.shortly.R
import com.mobven.shortly.adapter.ShortLinkPagingAdapter
import com.mobven.shortly.databinding.FragmentMylistBinding
import com.mobven.shortly.ui.main.MainViewModel
import com.mobven.shortly.utils.SpaceItemDecoration
import com.mobven.shortly.utils.share
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MyLinksFragment : Fragment() {
    private lateinit var binding: FragmentMylistBinding
    private val viewModel: MainViewModel by activityViewModels()
    private var toast: Toast? = null

    @Inject
    lateinit var clipBoardManager: ClipboardManager

    @Inject
    lateinit var shortLinkPagingAdapter: ShortLinkPagingAdapter

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
        binding.rvLinks.apply {
            adapter = shortLinkPagingAdapter
            setHasFixedSize(true)
            addItemDecoration(
                SpaceItemDecoration(
                    resources.getDimensionPixelSize(R.dimen._12sdp),
                    false
                )
            )
        }
        viewModel.apply {
            linkList.observe(viewLifecycleOwner) {
                shortLinkPagingAdapter.submitData(lifecycle, it)
            }

            shortLinkPagingAdapter.itemClickListener = {
                selectedShortenData(true, it.code)
                shortLinkPagingAdapter.copiedItem = it.code
                val clip = ClipData.newPlainText("Copied", it.short_link)
                clipBoardManager.setPrimaryClip(clip)
            }

            shortLinkPagingAdapter.itemShareListener = {
                requireContext().share(it.short_link, "Share")
            }

            shortLinkPagingAdapter.itemRemoveListener = { code, shortLink ->
                if (clipBoardManager.primaryClip?.getItemAt(0)?.text?.toString()
                        .equals(shortLink) && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                ) {
                    clipBoardManager.clearPrimaryClip()
                }
                deleteLink(code)
            }

            shortLinkPagingAdapter.openUrl = {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(browserIntent)
            }

            shortLinkPagingAdapter.addOnPagesUpdatedListener {
                if (shortLinkPagingAdapter.snapshot().isEmpty())
                    setEmptyState()
            }

            shortLinkPagingAdapter.itemQrCodeListener = {
                qrDialog(it.qr_code)
            }

            deleteError.observe(viewLifecycleOwner) {
                toast?.cancel()
                toast = Toast.makeText(context, "Silerken Bir Hata Oluştu!", Toast.LENGTH_SHORT)
                toast?.show()

            }
        }

    }

    fun qrDialog(bitmap: Bitmap) {
        val dialogBuilder = Dialog(requireContext())
        dialogBuilder.apply {
            setContentView(R.layout.popup_dialog_qr)
            var imgQrCode = findViewById<ImageView>(R.id.img_qr_code)
            imgQrCode.setImageBitmap(bitmap)
            show()
        }
    }

}