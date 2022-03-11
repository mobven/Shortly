package com.example.samplerunproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.samplerunproject.Result
import com.example.samplerunproject.databinding.ItemShortLinkBinding

class ShortLinkAdapter:RecyclerView.Adapter<ShortLinkAdapter.ViewHolder>() {

    private val shortLinkList = mutableListOf<Result>()
    var itemClickListener: (String) -> Unit = {}
    var itemRemoveListener: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemShortLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(shortLinkList[position])
    }

    override fun getItemCount(): Int = shortLinkList.size

    fun setData(list: List<Result>) {
        shortLinkList.clear()
        shortLinkList.addAll(list)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: ItemShortLinkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Result) {
            binding.apply {
                tvLongLink.text = item.full_share_link
                tvShortLink.text = item.full_short_link
                btnCopy.setOnClickListener {
                    itemClickListener(tvShortLink.text.toString())
                }
                icTrash.setOnClickListener{
                    itemRemoveListener(item.code)
                }
            }
        }
    }
}