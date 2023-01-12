package com.mobven.shortly.domain.usecase

import com.mobven.shortly.data.repository.MainRepository
import javax.inject.Inject

class GetSelectedOldUseCase @Inject constructor(
    private val mainRepository: MainRepository,
) {
    suspend operator fun invoke():String? =  mainRepository.getOldSelected()
}