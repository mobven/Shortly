package com.mobven.shortly.domain.usecase

import com.mobven.shortly.ShortenData
import com.mobven.shortly.data.repository.MainRepository
import com.mobven.shortly.domain.usecase.base.FlowUseCase
import com.mobven.shortly.executor.PostExecutorThread
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLinksUseCase @Inject constructor(
    private val mainRepository: MainRepository,
    private val postExecution: PostExecutorThread
) : FlowUseCase<Nothing?, List<ShortenData>>() {

    override val dispatcher: CoroutineDispatcher
        get() = postExecution.io

    override fun execute(params: Nothing?): Flow<List<ShortenData>> = mainRepository.getLinks()
}