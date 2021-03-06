package com.mobven.shortly.domain.usecase

import com.mobven.shortly.ShortenData
import com.mobven.shortly.data.repository.MainRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class GetLinksUseCase @Inject constructor(
    private val mainRepository: MainRepository,
) {
    fun invoke(): Flow<List<ShortenData>> = mainRepository.getLinks()
}