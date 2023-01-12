package com.mobven.shortly.ui.main

import com.mobven.shortly.ShortenData

data class MainUiState(
    val dataList: List<ShortenData> = emptyList(),
    val isLoading: Boolean = false
)