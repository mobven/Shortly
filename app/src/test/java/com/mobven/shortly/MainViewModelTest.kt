package com.mobven.shortly

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import app.cash.turbine.test
import com.mobven.shortly.domain.usecase.*
import com.mobven.shortly.ui.main.MainViewModel
import com.mobven.shortly.ui.main.ShortlyUiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {


    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var getLinksPagingDataFlowUseCase: GetLinksPagingDataFlowUseCase

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

    @MockK
    private lateinit var mainViewModel: MainViewModel

    private lateinit var testDispatcher:TestDispatcher

    private val givenData = PagingData.from(listOf(
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
    ))

    init {
        MockKAnnotations.init(this)
    }

    @Before
    fun setup() = runTest {
        try {
            testDispatcher = StandardTestDispatcher(testScheduler)
            Dispatchers.setMain(testDispatcher)

            coEvery { mainViewModel.viewModelScope } returns CoroutineScope(testDispatcher)

            mainViewModel = MainViewModel(
                shortenLinkUseCase,
                getLinksPagingDataFlowUseCase,
                insertLinkUseCase,
                updateShortenDataUseCase,
                getSelectedOldUseCase,
                deleteLinkUseCase
            )
            coEvery { getLinksPagingDataFlowUseCase.invoke("") } returns flowOf(givenData)
            advanceUntilIdle()
            assertEquals(mainViewModel.uiState.value, ShortlyUiState.Success(givenData))
            assertEquals(givenData, mainViewModel.linkList.value)
        } finally {
            Dispatchers.resetMain()
        }
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
            testDispatcher = StandardTestDispatcher(testScheduler)
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

            mainViewModel.uiState.test {

                //When
                mainViewModel.shortenLink(originalLink)

                //Then
                cancelAndConsumeRemainingEvents()
            }
            // turbine yerine bunu da kullanabiliriz advanceUntilIdle()
            assertEquals(
                mainViewModel.uiState.value,
                ShortlyUiState.LinkShorten(fakeResponse.data?.result!!)
            )

        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun deleteLinkTest() = runTest {
        try {
            testDispatcher = StandardTestDispatcher(testScheduler)
            Dispatchers.setMain(testDispatcher)
            //Given
            val code = "google.com"
            coEvery { deleteLinkUseCase.deleteLink(code) } returns 0

            //When
            mainViewModel.deleteLink(code)

            //Then
            advanceUntilIdle()
            assertEquals(mainViewModel.deleteError.value,true)

        }
        finally {
            Dispatchers.resetMain()
        }

    }

}
