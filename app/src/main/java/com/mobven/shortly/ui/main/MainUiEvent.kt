package com.mobven.shortly.ui.main

import com.mobven.shortly.ShortenData

sealed class MainUiEvent {

    data class ShowError(val message: String) : MainUiEvent()

    data class LinkShorten(val data: ShortenData) : MainUiEvent()

}