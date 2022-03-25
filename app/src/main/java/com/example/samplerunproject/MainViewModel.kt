package com.example.samplerunproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samplerunproject.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val repository: MainRepository
): ViewModel() {

    val shortLiveData = MutableLiveData<List<Result>>()

    fun getShortLinkData() {

        viewModelScope.launch {
            repository.getLinkListForDB().collect {
                shortLiveData.value = it
            }
        }
    }
}