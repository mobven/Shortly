package com.mobven.shortly.domain.usecase

import com.mobven.shortly.ShortenData
import com.mobven.shortly.data.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLinksUseCase @Inject constructor(
    private val mainRepository: MainRepository,
) {
    suspend fun invoke(): List<ShortenData> = mainRepository.getLinks()
}