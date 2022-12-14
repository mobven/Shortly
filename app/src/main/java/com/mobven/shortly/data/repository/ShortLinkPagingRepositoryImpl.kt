package com.mobven.shortly.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mobven.shortly.ShortenData
import com.mobven.shortly.data.room.LinkDao
import com.mobven.shortly.domain.ShortLinkPagingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShortLinkPagingRepositoryImpl @Inject constructor(private val linkDao: LinkDao) :
    ShortLinkPagingRepository {
    override suspend fun getShortLinkList(): Flow<PagingData<ShortenData>> {
        return Pager(
                config = PagingConfig(
                    pageSize = PAGE_SIZE
                ),
                pagingSourceFactory = {linkDao.getLinkList()}
            ).flow
    }

    companion object {
        const val PAGE_SIZE = 5
    }
}