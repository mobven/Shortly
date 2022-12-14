package com.mobven.shortly.ui.main

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.mobven.shortly.BaseResponse
import com.mobven.shortly.ShortenData
import com.mobven.shortly.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

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
                .collectLatest {
                if (true) {
                    _uiState.value = ShortlyUiState.Success(it)
                    _linkList.value = it
                } else
                    _uiState.value = ShortlyUiState.Empty(Unit)
            }
        }
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
}

sealed class ShortlyUiState {
    data class Empty(val unit: Unit) : ShortlyUiState()
    data class Error(val message: String) : ShortlyUiState()
    data class Success(val dataList: PagingData<ShortenData>) : ShortlyUiState()
    data class Loading(val unit: Unit) : ShortlyUiState()
    data class LinkShorten(val data: ShortenData) : ShortlyUiState()
}
