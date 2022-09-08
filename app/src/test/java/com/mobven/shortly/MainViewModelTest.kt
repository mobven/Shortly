package com.mobven.shortly

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mobven.shortly.domain.usecase.*
import com.mobven.shortly.ui.main.MainViewModel
import com.mobven.shortly.ui.main.ShortlyUiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    private lateinit var mainViewModel: MainViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
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
        assertEquals(mainViewModel.linkList.value, givenData)
    }

    @Test
    fun buttonClickTest() {
        //Given
        val isBlank = true

        //When
        mainViewModel.buttonClicked(isBlank)

        //Then
        assertTrue(mainViewModel.isBlank.value!!)
    }

    @Test
    fun shortenLinkTest() = runTest {
        try {
            val testDispatcher = StandardTestDispatcher(testScheduler)
            Dispatchers.setMain(testDispatcher)

            //Given
            val originalLink = "www.google.com"
            val fakeResponse: BaseResponse<Response> = BaseResponse(
                data = Response(
                    true, ShortenData(
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
                ), status = Status.SUCCESS, errorMessage = null
            )

            coEvery { shortenLinkUseCase.invoke(originalLink) } returns flowOf(fakeResponse)


            //When
            mainViewModel.shortenLink(originalLink)

            delay(1000)

            //Then
            assertEquals(
                mainViewModel.uiState.value,
                ShortlyUiState.LinkShorten(fakeResponse.data?.result!!)
            )

        } finally {
            Dispatchers.resetMain()
        }
    }
}