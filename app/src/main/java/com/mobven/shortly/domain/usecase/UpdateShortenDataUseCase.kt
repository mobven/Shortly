package com.mobven.shortly.domain.usecase

import com.mobven.shortly.data.repository.MainRepository
import javax.inject.Inject

class UpdateShortenDataUseCase @Inject constructor(
    private val mainRepository: MainRepository,
) {
    suspend operator fun invoke(isSelected: Boolean, code: String) {
        mainRepository.updateSelected(isSelected, code)
    }
}