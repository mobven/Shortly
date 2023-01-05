package com.mobven.shortly.ui.list

import com.mobven.shortly.ShortenData

data class MyLinksUiState(
    val dataList: List<ShortenData> = emptyList()
)