package com.mobven.shortly.ui.list

import androidx.paging.PagingData
import com.mobven.shortly.ShortenData

data class MyLinksUiState(
    val dataList: PagingData<ShortenData> = PagingData.empty()
)