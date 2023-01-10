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
    var itemClickListener: (ShortenData) -> Unit = {}
    var itemShareListener: (ShortenData) -> Unit = {}
    var itemRemoveListener: ((String), (String)) -> Unit = { _, _ -> }
    var itemFavoriteListener: ((Boolean), (String)) -> Unit = { _, _ -> }
    var openUrl: (String) -> Unit = {}
    var copiedItem: String? = null

    override fun onBindViewHolder(holder: ShortLinkPagingAdapter.ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
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
        fun bind(shortenData: ShortenData) {
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
                    itemClickListener(shortenData)
                }

                btnShare.click {
                    itemShareListener(shortenData)
                }

                icTrash.setOnClickListener {
                    itemRemoveListener(shortenData.code, shortenData.short_link)
                }
                cbFavorite.setOnClickListener {
                    itemFavoriteListener(cbFavorite.isChecked, shortenData.code)
                }
                if (shortenData.isSelected) {
                    btnCopy.alpha = 0.5f
                    btnCopy.text = root.context.getString(R.string.btn_copied)
                } else {
                    btnCopy.alpha = 1.0f
                    btnCopy.text = root.context.getString(R.string.btn_copy)
                }

                cbFavorite.isChecked = shortenData.isFavorite
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