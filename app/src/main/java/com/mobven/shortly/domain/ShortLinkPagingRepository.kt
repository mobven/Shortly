package com.mobven.shortly.domain

import androidx.paging.PagingData
import com.mobven.shortly.ShortenData
import kotlinx.coroutines.flow.Flow

interface ShortLinkPagingRepository {
    suspend fun getShortLinkList(): Flow<PagingData<ShortenData>>
}