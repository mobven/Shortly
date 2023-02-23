package com.mobven.shortly.main

import com.mobven.shortly.analytics.AnalyticsManager
import com.mobven.shortly.domain.usecase.GetLinksFlowUseCase
import com.mobven.shortly.domain.usecase.InsertLinkUseCase
import com.mobven.shortly.domain.usecase.ShortenLinkUseCase
import com.mobven.shortly.ui.main.MainViewModel
import com.mobven.shortly.utils.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @MockK
    private lateinit var shortenLinkUseCase: ShortenLinkUseCase

    @MockK
    private lateinit var getLinksFlowUseCase: GetLinksFlowUseCase

    @RelaxedMockK
    private lateinit var insertLinkUseCase: InsertLinkUseCase

    @RelaxedMockK
    private lateinit var analyticsManager: AnalyticsManager

    @InjectMockKs
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }


}