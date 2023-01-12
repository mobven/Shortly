package com.mobven.shortly.domain.usecase

import com.mobven.shortly.data.repository.MainRepository
import javax.inject.Inject

class DeleteLinkUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {

    suspend operator fun invoke(code: String): Boolean {
        val result = mainRepository.deleteLink(code)

        return result != ERROR_RESULT
    }

    companion object {
        private const val ERROR_RESULT = 0
    }
}