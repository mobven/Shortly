package com.example.samplerunproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samplerunproject.repository.MainRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException

@HiltViewModel
class MainViewModel @Inject constructor(
    val repository: MainRepository
): ViewModel() {

    val shortLiveData = MutableLiveData<List<Result>>()

    private var _result = MutableLiveData<Result>()
    val result:LiveData<Result> get() = _result

    private var _error = MutableLiveData<Boolean>()
    val error:LiveData<Boolean> get() = _error

    private var _toast = MutableLiveData<String>()
    val toast: LiveData<String> get() = _toast

    fun getShortLinkData() {

        viewModelScope.launch {
            repository.getLinkListForDB().collect {
                shortLiveData.value = it
            }
        }
    }

    fun callResponse(url: String?) {
        repository.getLinkFromRemote(url).enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                val result = response.body()?.result
                result?.let {
                    _result.value = it
                }?: kotlin.run {
                    val gson = Gson()
                    val error = gson.fromJson(response.errorBody()?.string(), Error::class.java)
                    _toast.value = error.error
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                when (t) {
                    is SocketTimeoutException -> {
                        _error.value = true
                    }
                    is SSLHandshakeException ->{
                        _error.value = true
                    }
                    else -> {

                    }
                }
            }
        })
    }
}