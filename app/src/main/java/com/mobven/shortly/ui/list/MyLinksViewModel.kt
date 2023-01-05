package com.mobven.shortly.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobven.shortly.domain.usecase.DeleteLinkUseCase
import com.mobven.shortly.domain.usecase.GetLinksUseCase
import com.mobven.shortly.domain.usecase.GetSelectedOldUseCase
import com.mobven.shortly.domain.usecase.UpdateShortenDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyLinksViewModel @Inject constructor(
    private val getLinksUseCase: GetLinksUseCase,
    private val updateShortenDataUseCase: UpdateShortenDataUseCase,
    private val getSelectedOldUseCase: GetSelectedOldUseCase,
    private val deleteLinkUseCase: DeleteLinkUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyLinksUiState())
    val uiState: StateFlow<MyLinksUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<MyLinksUiEvent>()
    val uiEvent: SharedFlow<MyLinksUiEvent> = _uiEvent.asSharedFlow()

    init {
        getLocalShortenLink()
    }

    private fun getLocalShortenLink() {
        viewModelScope.launch {
            getLinksUseCase().distinctUntilChanged()
                .collect {
                    if (it.isNotEmpty()) {
                        _uiState.update { state -> state.copy(dataList = it) }
                    } else
                        _uiState.update { state -> state.copy(dataList = emptyList()) }
                }
        }
    }

    fun delete(code: String) {
        viewModelScope.launch {
            val isSuccess = deleteLinkUseCase(code)
            if (isSuccess.not()) {
                _uiEvent.emit(MyLinksUiEvent.ShowError("Silerken Bir Hata Oluştu!"))
            }

            getLocalShortenLink()
        }
    }

    fun selectedShortenData(isSelected: Boolean, code: String) {
        viewModelScope.launch {
            getSelectedOldUseCase()?.let {
                updateShortenDataUseCase(false, it)
            }
            updateShortenDataUseCase(isSelected, code)
            getLocalShortenLink()
        }
    }
}
