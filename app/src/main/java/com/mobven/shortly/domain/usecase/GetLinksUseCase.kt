package com.mobven.shortly.domain.usecase

import androidx.paging.PagingData
import com.mobven.shortly.ShortenData
import com.mobven.shortly.data.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLinksUseCase @Inject constructor(
    private val mainRepository: MainRepository,
) {
    suspend fun invoke(): Flow<PagingData<ShortenData>> = mainRepository.getLinks()
}