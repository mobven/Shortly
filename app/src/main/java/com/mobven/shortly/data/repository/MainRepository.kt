package com.mobven.shortly.data.repository

import com.mobven.shortly.BaseResponse
import com.mobven.shortly.Response
import com.mobven.shortly.ShortenData
import com.mobven.shortly.data.ShortlyLocalDataSource
import com.mobven.shortly.data.ShortlyRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface MainRepository {
    fun shortenLink(editLink: String): Flow<BaseResponse<Response>>
    fun getLinks(): Flow<List<ShortenData>>
    suspend fun insertLink(shortenData: ShortenData)
}

@Singleton
class MainRepositoryImpl @Inject constructor(
    private val remoteDataSource: ShortlyRemoteDataSource,
    private val localDataSource: ShortlyLocalDataSource
) : MainRepository {
    override fun shortenLink(editLink: String) = remoteDataSource.shortenLink(editLink)
    override fun getLinks() = localDataSource.getLinks()
    override suspend fun insertLink(shortenData: ShortenData) = localDataSource.insertLink(shortenData)
}