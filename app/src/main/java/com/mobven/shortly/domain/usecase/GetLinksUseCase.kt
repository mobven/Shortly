package com.mobven.shortly.domain.usecase

import androidx.paging.PagingData
import com.mobven.shortly.ShortenData
import com.mobven.shortly.domain.ShortLinkPagingRepository
import com.mobven.shortly.domain.usecase.base.FlowUseCase
import com.mobven.shortly.executor.PostExecutorThread
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLinksUseCase @Inject constructor(
    private val shortLinkPagingRepository: ShortLinkPagingRepository,
    private val postExecution: PostExecutorThread
) : FlowUseCase<Nothing?, PagingData<ShortenData>>() {

    override val dispatcher: CoroutineDispatcher
        get() = postExecution.io

    override fun execute(params: Nothing?): Flow<PagingData<ShortenData>> = shortLinkPagingRepository.getShortLinkList()
}