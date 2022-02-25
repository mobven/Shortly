package com.example.samplerunproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.samplerunproject.R
import com.example.samplerunproject.databinding.ItemShortLinkBinding

class ShortLinkAdapter:RecyclerView.Adapter<ShortLinkAdapter.ViewHolder>() {

    private val shortLinkList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShortLinkBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(shortLinkList[position])
    }

    override fun getItemCount(): Int = shortLinkList.size



    inner class ViewHolder(val binding:ItemShortLinkBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {

        }
    }
}