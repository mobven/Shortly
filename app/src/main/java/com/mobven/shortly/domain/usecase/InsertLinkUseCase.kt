package com.mobven.shortly.domain.usecase

import com.mobven.shortly.ShortenData
import com.mobven.shortly.data.repository.MainRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class InsertLinkUseCase @Inject constructor(
    private val mainRepository: MainRepository,
) {
    suspend fun invokeInsert(shortenData: ShortenData){
        mainRepository.insertLink(shortenData)
    }
}