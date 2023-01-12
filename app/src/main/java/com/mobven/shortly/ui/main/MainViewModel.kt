package com.mobven.shortly.ui.main

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobven.shortly.BaseResponse
import com.mobven.shortly.ShortenData
import com.mobven.shortly.domain.usecase.GetLinksFlowUseCase
import com.mobven.shortly.domain.usecase.GetLinksPagingDataFlowUseCase
import com.mobven.shortly.domain.usecase.InsertLinkUseCase
import com.mobven.shortly.domain.usecase.ShortenLinkUseCase
import com.mobven.shortly.ui.main.MainUiEvent.ShowError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.glxn.qrgen.android.QRCode
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val shortenLinkUseCase: ShortenLinkUseCase,
    private val getLinksFlowUseCase: GetLinksFlowUseCase,
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
        getLinksFlowUseCase()
            .distinctUntilChanged()
            .onStart { _uiState.update { state -> state.copy(isLoading = true) } }
            .onEach { _uiState.update { state -> state.copy(dataList = it, isLoading = false) } }
            .launchIn(viewModelScope)
    }

    fun shortenLink(originalLink: String) {
        shortenLinkUseCase(originalLink)
            .onStart { _uiState.update { state -> state.copy(isLoading = true) } }
            .filter { it.data?.ok == true }
            .mapNotNull { BaseResponse.success(it.data).data }
            .onEach { _uiEvent.emit(MainUiEvent.LinkShorten(it.result)) }
            .onCompletion { _uiState.update { state -> state.copy(isLoading = false) } }
            .catch { _uiEvent.emit(ShowError(it.message.orEmpty())) }
            .launchIn(viewModelScope)
    }


    fun insertLink(shortenData: ShortenData) {
        viewModelScope.launch {
            shortenData.qr_code = createQrCode(shortenData.original_link)
            insertLinkUseCase.invokeInsert(shortenData)
            getLocalShortenLink()
        }
    }

    fun selectedShortenData(isSelected: Boolean, code: String) {
        viewModelScope.launch {
            getSelectedOldUseCase.getSelectedOld()?.let {
                updateShortenDataUseCase.updateSelected(false, it)
            }
            updateShortenDataUseCase.updateSelected(isSelected, code)
            getLocalShortenLink()
        }
    }

    fun deleteLink(code: String) {
        viewModelScope.launch {
            val result = deleteLinkUseCase.deleteLink(code)
            if (result == 0)
                _deleteError.value = true
            getLocalShortenLink()
        }
    }

    fun createQrCode(url: String): Bitmap {
        val qrCode = QRCode.from(url).bitmap()
        return qrCode
    }
}

sealed class ShortlyUiState {
    data class Empty(val unit: Unit) : ShortlyUiState()
    data class Error(val message: String) : ShortlyUiState()
    data class Success(val dataList: PagingData<ShortenData>) : ShortlyUiState()
    data class Loading(val unit: Unit) : ShortlyUiState()
    data class LinkShorten(val data: ShortenData) : ShortlyUiState()
            insertLinkUseCase(shortenData)
        }
    }
}
