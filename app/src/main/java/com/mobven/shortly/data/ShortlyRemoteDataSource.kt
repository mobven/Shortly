package com.mobven.shortly.data

import com.mobven.shortly.Response
import com.mobven.shortly.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface ShortlyRemoteDataSource {
    fun shortenLink(originalLink: String): Flow<Response>
}

class ShortlyRemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : ShortlyRemoteDataSource {
    override fun shortenLink(originalLink: String): Flow<Response> = flow {
        emit(apiService.getLinks(originalLink))
    }.flowOn(Dispatchers.IO)
}