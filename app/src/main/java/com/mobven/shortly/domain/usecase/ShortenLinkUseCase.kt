package com.mobven.shortly.domain.usecase

import com.mobven.shortly.BaseResponse
import com.mobven.shortly.Response
import com.mobven.shortly.data.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShortenLinkUseCase @Inject constructor(
    private val mainRepository: MainRepository,
) {
    fun invoke(input: String): Flow<BaseResponse<Response>> = mainRepository.shortenLink(input)
}