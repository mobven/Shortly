package com.mobven.shortly.domain.usecase

import com.mobven.shortly.BaseResponse
import com.mobven.shortly.Response
import com.mobven.shortly.data.repository.MainRepository
import com.mobven.shortly.domain.usecase.base.FlowUseCase
import com.mobven.shortly.executor.PostExecutorThread
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShortenLinkUseCase @Inject constructor(
    private val mainRepository: MainRepository,
    private val postExecution: PostExecutorThread
) : FlowUseCase<String, BaseResponse<Response>>() {

    override val dispatcher: CoroutineDispatcher
        get() = postExecution.io

    override fun execute(params: String?): Flow<BaseResponse<Response>> =  mainRepository.shortenLink(params)
}