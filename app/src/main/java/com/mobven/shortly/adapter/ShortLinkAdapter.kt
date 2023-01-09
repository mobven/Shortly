package com.mobven.shortly.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobven.extension.click
import com.mobven.shortly.R
import com.mobven.shortly.ShortenData
import com.mobven.shortly.databinding.ItemShortLinkBinding
import com.mobven.shortly.utils.underLineText
import dagger.hilt.android.scopes.FragmentScoped

@FragmentScoped
class ShortLinkAdapter(var shortLinkList: List<ShortenData>) : RecyclerView.Adapter<ShortLinkAdapter.ViewHolder>() {
    var itemClickListener: (ShortenData) -> Unit = {}
    var itemShareListener: (ShortenData) -> Unit = {}
    var itemRemoveListener: ((String), (String)) -> Unit = { _, _ -> }
    var itemFavoriteListener: ((Boolean), (String)) -> Unit = { _, _ -> }
    var openUrl: (String) -> Unit = {}
    var copiedItem: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemShortLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(shortLinkList[position])
    }

    fun setData(newList: List<ShortenData>) {
        val diffUtil = ShortLinkDiffUtil(shortLinkList, newList)
        val result = DiffUtil.calculateDiff(diffUtil)
        result.dispatchUpdatesTo(this)
        shortLinkList = newList as MutableList<ShortenData>
    }

    inner class ViewHolder(private val binding: ItemShortLinkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShortenData) {
            binding.apply {
                tvLongLink.underLineText(item.original_link)
                tvShortLink.underLineText(item.full_short_link)

                tvLongLink.setOnClickListener {
                    openUrl(item.original_link)
                }

                tvShortLink.setOnClickListener {
                    openUrl(item.original_link)
                }

                btnCopy.setOnClickListener() {
                    itemClickListener(item)
                }

                btnShare.click {
                    itemShareListener(item)
                }

                icTrash.setOnClickListener {
                    itemRemoveListener(item.code, item.short_link)
                }
                cbFavorite.setOnClickListener {
                    itemFavoriteListener(cbFavorite.isChecked, item.code)
                }
                if (item.isSelected) {
                    btnCopy.alpha = 0.5f
                    btnCopy.text = root.context.getString(R.string.btn_copied)
                } else {
                    btnCopy.alpha = 1.0f
                    btnCopy.text = root.context.getString(R.string.btn_copy)
                }

                cbFavorite.isChecked = item.isFavorite
            }
        }
    }

    override fun getItemCount(): Int = shortLinkList.size

}