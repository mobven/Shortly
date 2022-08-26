package com.mobven.shortly.adapter

import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobven.shortly.R
import com.mobven.shortly.ShortenData
import com.mobven.shortly.databinding.ItemShortLinkBinding
import com.mobven.shortly.utils.underLineText
import dagger.hilt.android.scopes.FragmentScoped

@FragmentScoped
class ShortLinkAdapter : RecyclerView.Adapter<ShortLinkAdapter.ViewHolder>() {
    var itemClickListener: (ShortenData) -> Unit = {}
    var itemRemoveListener: ((String), (String)) -> Unit = { _, _ -> }
    var openUrl: (String) -> Unit = {}
    private var shortLinkList = mutableListOf<ShortenData>()
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
        shortLinkList = newList as MutableList<ShortenData>
        result.dispatchUpdatesTo(this)
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
                icTrash.setOnClickListener {
                    itemRemoveListener(item.code, item.short_link)
                }
                if (item.isSelected) {
                    btnCopy.setBackgroundColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.dark_violet
                        )
                    )
                    btnCopy.text = root.context.getString(R.string.btn_copied)
                } else {
                    if (binding.root.context.resources.getConfiguration().uiMode and
                        Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)
                        btnCopy.setBackgroundColor(ContextCompat.getColor(root.context,R.color.teal_dark))
                    else
                        btnCopy.setBackgroundColor(ContextCompat.getColor(root.context,R.color.teal_200))

                    btnCopy.text = root.context.getString(R.string.btn_copy)
                }
            }
        }
    }

    override fun getItemCount(): Int = shortLinkList.size

}