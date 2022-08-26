package com.mobven.shortly

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mobven.shortly.domain.usecase.*
import com.mobven.shortly.ui.main.MainViewModel
import com.mobven.shortly.ui.main.ShortlyUiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    private lateinit var mainViewModel: MainViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK(relaxed = true)
    lateinit var getLinksUseCase: GetLinksUseCase

    @MockK
    lateinit var shortenLinkUseCase: ShortenLinkUseCase

    @MockK
    lateinit var insertLinkUseCase: InsertLinkUseCase

    @MockK
    lateinit var updateShortenDataUseCase: UpdateShortenDataUseCase

    @MockK
    lateinit var getSelectedOldUseCase: GetSelectedOldUseCase

    @MockK
    lateinit var deleteLinkUseCase: DeleteLinkUseCase

    private val givenData = listOf(
        ShortenData(
            "sdf",
            "sdf",
            "dfg",
            "dfgdf",
            "sdgfd",
            "rter",
            "gd",
            "gdfgdf",
            false
        )
    )

    init {
        MockKAnnotations.init(this)
    }

    @Before
    fun setup() = runTest {
        try {
            val testDispatcher = StandardTestDispatcher(testScheduler)
            Dispatchers.setMain(testDispatcher)

            mainViewModel = MainViewModel(
                shortenLinkUseCase,
                getLinksUseCase,
                insertLinkUseCase,
                updateShortenDataUseCase,
                getSelectedOldUseCase,
                deleteLinkUseCase
            )

            coEvery { getLinksUseCase.invoke() } returns flowOf(givenData)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun initTest() = runTest {
        assertEquals(mainViewModel.uiState.value, ShortlyUiState.Success(givenData))
    }
}