package com.mobven.shortly.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobven.shortly.Response
import com.mobven.shortly.ShortenData
import com.mobven.shortly.domain.usecase.GetLinksUseCase
import com.mobven.shortly.domain.usecase.ShortenLinkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val shortenLinkUseCase: ShortenLinkUseCase,
    private val getLinksUseCase: GetLinksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<List<ShortenData>>(emptyList())
    val uiState: StateFlow<List<ShortenData>> = _uiState

    init {
        viewModelScope.launch {
            getLinksUseCase.invoke()
                .collect {
                    _uiState.value = it
                }
        }
    }

    private val _linkListLiveData = MutableLiveData<List<ShortenData>>()
    val linkListLiveData: MutableLiveData<List<ShortenData>> = _linkListLiveData

    private val _shortenLinkLiveData = MutableLiveData<Response>()
    val shortenLinkLiveData: LiveData<Response> = _shortenLinkLiveData

/*    fun getShortLinkData() {
        viewModelScope.launch {
            _linkListLiveData.value = getLinksFromLocalDBUseCase.invoke()
        }
    }*/

    /*fun shortenLink(originalLink: String) {
        viewModelScope.launch {
            _shortenLinkLiveData.value = shortenLinkUseCase.invoke(originalLink)
        }
    }*/
}