package com.mobven.shortly.domain.usecase

import com.mobven.shortly.data.repository.MainRepository
import com.mobven.shortly.executor.PostExecutorThread
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateFavoriteUseCase @Inject constructor(
    private val mainRepository: MainRepository,
    private val postExecution: PostExecutorThread
) {
    suspend operator fun invoke(isFavorite: Boolean, code: String) {
        withContext(postExecution.io) {
            mainRepository.updateFavorite(isFavorite, code)
        }
    }
}