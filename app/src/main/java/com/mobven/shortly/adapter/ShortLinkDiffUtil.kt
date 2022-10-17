package com.mobven.shortly.adapter

import androidx.recyclerview.widget.DiffUtil
import com.mobven.shortly.ShortenData

class ShortLinkDiffUtil(private val oldList: List<ShortenData>, private val newList: List<ShortenData>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] === newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}