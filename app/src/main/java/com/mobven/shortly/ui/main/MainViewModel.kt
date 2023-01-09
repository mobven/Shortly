package com.mobven.shortly.ui.main

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.mobven.shortly.BaseResponse
import com.mobven.shortly.ShortenData
import com.mobven.shortly.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.glxn.qrgen.android.QRCode
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val shortenLinkUseCase: ShortenLinkUseCase,
    private val getLinksUseCase: GetLinksUseCase,
    private val insertLinkUseCase: InsertLinkUseCase,
    private val updateShortenDataUseCase: UpdateShortenDataUseCase,
    private val getSelectedOldUseCase: GetSelectedOldUseCase,
    private val deleteLinkUseCase: DeleteLinkUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ShortlyUiState>(ShortlyUiState.Empty(Unit))
    val uiState: StateFlow<ShortlyUiState> = _uiState

    private var _linkList = MutableLiveData<PagingData<ShortenData>>()
    val linkList: LiveData<PagingData<ShortenData>> get() = _linkList

    private var _deleteError = MutableLiveData<Boolean>()
    val deleteError: LiveData<Boolean> get() = _deleteError

    private var _isBlank = MutableLiveData<Boolean>()
    val isBlank: LiveData<Boolean> get() = _isBlank

    init {
        getLocalShortenLink()
    }

    private fun getLocalShortenLink() {
        viewModelScope.launch {
            getLinksUseCase.invoke()
                .collectLatest  {
                    _uiState.value = ShortlyUiState.Success(it)
                    _linkList.value = it
            }
        }
    }

    fun setEmptyState() {
        _uiState.value = ShortlyUiState.Empty(Unit)
    }

    fun buttonClicked(isBlank: Boolean) {
        _isBlank.value = isBlank
    }

    fun shortenLink(originalLink: String) {
        viewModelScope.launch {
            shortenLinkUseCase.invoke(originalLink).catch {
                _uiState.value = ShortlyUiState.Error(it.message.orEmpty())
            }.collect {
                if (it.data?.ok == true)
                    BaseResponse.success(it.data).data?.let {
                        _uiState.value = ShortlyUiState.LinkShorten(it.result)
                    }
            }
        }
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
}
