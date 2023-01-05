package com.mobven.shortly.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobven.shortly.BaseResponse
import com.mobven.shortly.ShortenData
import com.mobven.shortly.domain.usecase.GetLinksUseCase
import com.mobven.shortly.domain.usecase.InsertLinkUseCase
import com.mobven.shortly.domain.usecase.ShortenLinkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val shortenLinkUseCase: ShortenLinkUseCase,
    private val getLinksUseCase: GetLinksUseCase,
    private val insertLinkUseCase: InsertLinkUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<MainUiEvent>()
    val uiEvent: SharedFlow<MainUiEvent> = _uiEvent.asSharedFlow()

    init {
        getLocalShortenLink()
    }

    private fun getLocalShortenLink() {
        viewModelScope.launch {
            _uiState.update { state -> state.copy(isLoading = true) }
            getLinksUseCase().distinctUntilChanged()
                .collect {
                    if (it.isNotEmpty()) {
                        _uiState.update { state -> state.copy(dataList = it) }
                    } else
                        _uiState.update { state -> state.copy(dataList = emptyList()) }
                }.also {
                    _uiState.update { state -> state.copy(isLoading = false) }
                }
        }
    }

    fun shortenLink(originalLink: String) {
        viewModelScope.launch {
            _uiState.update { state -> state.copy(isLoading = true) }
            shortenLinkUseCase.invoke(originalLink).catch {
                _uiEvent.emit(MainUiEvent.ShowError(it.message.orEmpty()))
            }.collect {
                if (it.data?.ok == true)
                    BaseResponse.success(it.data).data?.let {
                        _uiEvent.emit(MainUiEvent.LinkShorten(it.result))
                    }
            }.also {
                _uiState.update { state -> state.copy(isLoading = false) }
            }
        }
    }

    fun insertLink(shortenData: ShortenData) {
        viewModelScope.launch {
            insertLinkUseCase(shortenData)
            getLocalShortenLink()
        }
    }
}
