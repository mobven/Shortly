package com.example.samplerunproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.samplerunproject.databinding.ItemShortLinkBinding

class ShortLinkAdapter:RecyclerView.Adapter<ShortLinkAdapter.ViewHolder>() {

    private val shortLinkList = mutableListOf<String>()
    var itemClickListener: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemShortLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(shortLinkList[position])
    }

    override fun getItemCount(): Int = shortLinkList.size

    fun setData(list: MutableList<String>) {
        shortLinkList.clear()
        shortLinkList.addAll(list)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: ItemShortLinkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.apply {
                btnCopy.setOnClickListener {
                    itemClickListener(tvShortLink.text.toString())
                }
            }
        }
    }
}