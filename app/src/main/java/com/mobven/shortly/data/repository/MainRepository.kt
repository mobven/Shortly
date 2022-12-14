package com.mobven.shortly.data.repository

import androidx.paging.PagingData
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
    suspend fun getLinks(): Flow<PagingData<ShortenData>>
    suspend fun insertLink(shortenData: ShortenData)
    suspend fun updateSelected(isSelected: Boolean, code: String)
    suspend fun getOldSelected():String?
    suspend fun deleteLink(code: String): Int
}

@Singleton
class MainRepositoryImpl @Inject constructor(
    private val remoteDataSource: ShortlyRemoteDataSource,
    private val localDataSource: ShortlyLocalDataSource,
    // todo burayı daha iyi bir şekilde handle edebiliriz ileride bence
    private val shortLinkPagingRepositoryImpl: ShortLinkPagingRepositoryImpl
) : MainRepository {
    override fun shortenLink(editLink: String) = remoteDataSource.shortenLink(editLink)
    override suspend fun getLinks() = shortLinkPagingRepositoryImpl.getShortLinkList()
    override suspend fun insertLink(shortenData: ShortenData) = localDataSource.insertLink(shortenData)
    override suspend fun updateSelected(isSelected: Boolean, code: String) = localDataSource.updateSelected(isSelected, code)
    override suspend fun getOldSelected(): String? = localDataSource.getOldSelected()
    override suspend fun deleteLink(code: String): Int = localDataSource.deleteLink(code)
}