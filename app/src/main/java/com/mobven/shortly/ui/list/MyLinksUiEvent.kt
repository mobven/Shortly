package com.mobven.shortly.ui.list

sealed class MyLinksUiEvent {

    data class ShowError(val message: String) : MyLinksUiEvent()

}