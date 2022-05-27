package com.mobven.shortly.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobven.shortly.BaseResponse
import com.mobven.shortly.Error
import com.mobven.shortly.Response
import com.mobven.shortly.ShortenData
import com.mobven.shortly.domain.usecase.GetLinksUseCase
import com.mobven.shortly.domain.usecase.InsertLinkUseCase
import com.mobven.shortly.domain.usecase.ShortenLinkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val shortenLinkUseCase: ShortenLinkUseCase,
    private val getLinksUseCase: GetLinksUseCase,
    private val insertLinkUseCase: InsertLinkUseCase
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
    val shortenLinkLiveData: LiveData<Response> get() = _shortenLinkLiveData

    private val _shortenLinkErrorLiveData = MutableLiveData<Error>()
    val shortenLinkErrorLiveData get() = _shortenLinkErrorLiveData

    fun getShortLinkData() {
        viewModelScope.launch {
            getLinksUseCase.invoke().catch {
                print("Error")
            }.collect{
                _linkListLiveData.value = it
            }
        }
    }

    fun shortenLink(originalLink: String) {
        viewModelScope.launch {
            shortenLinkUseCase.invoke(originalLink).catch {
                _shortenLinkErrorLiveData.value = com.mobven.shortly.Error(it.message?:"")
            }.collect{
                if (it.data?.ok == true)
                    BaseResponse.success(it.data).data?.let {
                    _shortenLinkLiveData.value = it
                }
            }
        }
    }

    fun insertLink(shortenData: ShortenData) {
        viewModelScope.launch {
            insertLinkUseCase.invokeInsert(shortenData)
        }
    }
}