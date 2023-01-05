package com.mobven.shortly.domain.usecase

import androidx.paging.PagingData
import com.mobven.shortly.ShortenData
import com.mobven.shortly.domain.ShortLinkPagingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLinksUseCase @Inject constructor(
    private val shortLinkPagingRepository: ShortLinkPagingRepository,
) {
    suspend fun invoke(): Flow<PagingData<ShortenData>> = shortLinkPagingRepository.getShortLinkList()
}