package com.mobven.shortly.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mobven.shortly.ShortenData
import com.mobven.shortly.databinding.ItemShortLinkBinding

class ShortLinkAdapter:ListAdapter<ShortenData, ShortLinkAdapter.ViewHolder>(DiffShortLink()) {

    /*private val shortLinkList = mutableListOf<Result>()*/
    var itemClickListener: (String) -> Unit = {}
    var itemRemoveListener: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemShortLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

/*    override fun getItemCount(): Int = shortLinkList.size*/

   /* fun setData(list: List<Result>) {
        shortLinkList.clear()
        shortLinkList.addAll(list)
        //not recommended
        notifyDataSetChanged()
    }
*/

    inner class ViewHolder(private val binding: ItemShortLinkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShortenData) {
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

    class DiffShortLink() : DiffUtil.ItemCallback<ShortenData>(){
        override fun areItemsTheSame(oldItem: ShortenData, newItem: ShortenData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ShortenData, newItem: ShortenData): Boolean {
          return oldItem == newItem
        }

    }
}