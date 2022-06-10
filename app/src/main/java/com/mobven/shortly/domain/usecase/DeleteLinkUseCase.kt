package com.mobven.shortly.domain.usecase

import com.mobven.shortly.data.repository.MainRepository
import javax.inject.Inject

class DeleteLinkUseCase @Inject constructor(private val mainRepository: MainRepository) {
    suspend fun deleteLink(code: String):Int = mainRepository.deleteLink(code)
}