package com.mobven.shortly.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobven.shortly.R
import com.mobven.shortly.ShortenData
import com.mobven.shortly.databinding.ItemShortLinkBinding
import javax.inject.Inject

class ShortLinkAdapter: RecyclerView.Adapter<ShortLinkAdapter.ViewHolder>() {
    var itemClickListener: (ShortenData) -> Unit = {}
    var itemRemoveListener: (String) -> Unit = {}
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
                tvLongLink.text = item.full_share_link
                tvShortLink.text = item.full_short_link
                btnCopy.setOnClickListener {
                    itemClickListener(item)
                }
                icTrash.setOnClickListener {
                    itemRemoveListener(item.code)
                }
                if (copiedItem != null && copiedItem == item.code){
                    btnCopy.setBackgroundColor(ContextCompat.getColor(root.context, R.color.dark_violet))
                    btnCopy.text = root.context.getString(R.string.btn_copied)
                }
            }
        }
    }

    override fun getItemCount(): Int = shortLinkList.size

}