package com.mobven.shortly.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobven.shortly.BaseResponse
import com.mobven.shortly.ShortenData
import com.mobven.shortly.domain.usecase.GetLinksUseCase
import com.mobven.shortly.domain.usecase.InsertLinkUseCase
import com.mobven.shortly.domain.usecase.ShortenLinkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val shortenLinkUseCase: ShortenLinkUseCase,
    private val getLinksUseCase: GetLinksUseCase,
    private val insertLinkUseCase: InsertLinkUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ShortlyUiState>(ShortlyUiState.Empty(Unit))
    val uiState: StateFlow<ShortlyUiState> = _uiState

    private var _linkList = MutableLiveData<List<ShortenData>>()
    val linkList:LiveData<List<ShortenData>> get() = _linkList

    init {
        getLocalShortenLink()
    }

    private fun getLocalShortenLink(){
        viewModelScope.launch {
            getLinksUseCase.invoke()
                .distinctUntilChanged()
                .collect {
                    if (it.isNotEmpty()){
                        _uiState.value = ShortlyUiState.Success(it)
                        _linkList.value = it
                    }
                    else
                        _uiState.value = ShortlyUiState.Empty(Unit)
                }
        }
    }
    fun shortenLink(originalLink: String) {
        viewModelScope.launch {
            shortenLinkUseCase.invoke(originalLink).catch {
                _uiState.value = ShortlyUiState.Error(it.message.orEmpty())
            }.collect{
                if (it.data?.ok == true)
                    BaseResponse.success(it.data).data?.let {
                        _uiState.value = ShortlyUiState.LinkShorten(it.result)
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

sealed class ShortlyUiState {
    data class Empty(val unit: Unit) : ShortlyUiState()
    data class Error(val message: String) : ShortlyUiState()
    data class Success(val dataList: List<ShortenData>) : ShortlyUiState()
    data class Loading(val unit: Unit) : ShortlyUiState()
    data class LinkShorten(val data: ShortenData): ShortlyUiState()
}
