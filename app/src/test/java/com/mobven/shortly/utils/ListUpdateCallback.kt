package com.mobven.shortly.utils

import androidx.recyclerview.widget.ListUpdateCallback

val emptyCallback: ListUpdateCallback
    get() = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) = Unit
        override fun onRemoved(position: Int, count: Int) = Unit
        override fun onMoved(fromPosition: Int, toPosition: Int) = Unit
        override fun onChanged(position: Int, count: Int, payload: Any?) = Unit
    }