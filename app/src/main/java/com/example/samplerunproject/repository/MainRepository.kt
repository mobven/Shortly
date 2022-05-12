package com.example.samplerunproject.repository

import com.example.samplerunproject.Result
import com.example.samplerunproject.api.ApiService
import com.example.samplerunproject.room.LinkDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(val apiService: ApiService, val linkDao: LinkDao) {

    suspend fun getLinkListForDB() : Flow<List<Result>>{
        return flow {
            emit(linkDao.getLinkList())
        }.flowOn(Dispatchers.IO)
    }

    fun getLinkFromRemote(url: String?) = apiService.getLinks(url)
}