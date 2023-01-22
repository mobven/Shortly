package com.mobven.shortly.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobven.extension.click
import com.mobven.shortly.R
import com.mobven.shortly.ShortenData
import com.mobven.shortly.databinding.ItemShortLinkBinding
import com.mobven.shortly.utils.underLineText
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class ShortLinkPagingAdapter @Inject constructor() :
    PagingDataAdapter<ShortenData, ShortLinkPagingAdapter.ViewHolder>(ShortLinkDiffUtil) {
    var copyClickListener: ((ShortenData), (Int)) -> Unit = { _, _ ->}
    var itemShareListener: ((ShortenData), (Int)) -> Unit = {_,_ -> }
    var itemRemoveListener: ((String), (String), (Int)) -> Unit = { _, _, _ -> }
    var openUrl: (String) -> Unit = {}
    var copiedItem: String? = null

    override fun onBindViewHolder(holder: ShortLinkPagingAdapter.ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, position)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShortLinkPagingAdapter.ViewHolder {
        val binding =
            ItemShortLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemShortLinkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(shortenData: ShortenData, position: Int) {
            binding.apply {
                tvLongLink.underLineText(shortenData.original_link)
                tvShortLink.underLineText(shortenData.full_short_link)

                tvLongLink.setOnClickListener {
                    openUrl(shortenData.original_link)
                }

                tvShortLink.setOnClickListener {
                    openUrl(shortenData.original_link)
                }

                btnCopy.setOnClickListener() {
                    copyClickListener(shortenData, position)
                }

                btnShare.click {
                    itemShareListener(shortenData, position)
                }

                icTrash.setOnClickListener {
                    itemRemoveListener(shortenData.code, shortenData.short_link, position)
                }
                if (shortenData.isSelected) {
                    btnCopy.alpha = 0.5f
                    btnCopy.text = root.context.getString(R.string.btn_copied)
                } else {
                    btnCopy.alpha = 1.0f
                    btnCopy.text = root.context.getString(R.string.btn_copy)
                }
            }
        }

    }

    object ShortLinkDiffUtil : DiffUtil.ItemCallback<ShortenData>() {
        override fun areItemsTheSame(oldItem: ShortenData, newItem: ShortenData): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: ShortenData, newItem: ShortenData): Boolean {
            return oldItem == newItem
        }
    }
}