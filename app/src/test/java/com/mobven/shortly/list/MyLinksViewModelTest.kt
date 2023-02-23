package com.mobven.shortly.list

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.mobven.shortly.ShortenData
import com.mobven.shortly.adapter.ShortLinkPagingAdapter.ShortLinkDiffUtil
import com.mobven.shortly.domain.usecase.*
import com.mobven.shortly.ui.list.MyLinksUiEvent.ShowError
import com.mobven.shortly.ui.list.MyLinksViewModel
import com.mobven.shortly.utils.CoroutineTestRule
import com.mobven.shortly.utils.emptyCallback
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MyLinksViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @MockK
    private lateinit var getLinksPagingDataFlowUseCase: GetLinksPagingDataFlowUseCase

    @RelaxedMockK
    private lateinit var updateShortenDataUseCase: UpdateShortenDataUseCase

    @MockK
    private lateinit var getSelectedOldUseCase: GetSelectedOldUseCase

    @RelaxedMockK
    private lateinit var updateFavoriteUseCase: UpdateFavoriteUseCase

    @MockK
    private lateinit var deleteLinkUseCase: DeleteLinkUseCase

    private lateinit var myLinksViewModel: MyLinksViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        coroutineTestRule.runBlockingTest {
            val data = listOf<ShortenData>(mockk(), mockk(), mockk(), mockk())
            val pagingData = PagingData.from(data)

            every { getLinksPagingDataFlowUseCase() } returns flowOf(pagingData)

            myLinksViewModel = MyLinksViewModel(
                getLinksPagingDataFlowUseCase,
                updateShortenDataUseCase,
                getSelectedOldUseCase,
                updateFavoriteUseCase,
                deleteLinkUseCase
            )

            val differ = AsyncPagingDataDiffer(
                diffCallback = ShortLinkDiffUtil,
                updateCallback = emptyCallback,
                workerDispatcher = Dispatchers.Main
            )
            differ.submitData(myLinksViewModel.uiState.value.dataList)

            assertEquals(data, differ.snapshot().items)
        }
    }

    @Test
    fun `Verify favorite updated when favorite set`() =
        coroutineTestRule.runBlockingTest {
            val code = "code"
            val isFavorite = true

            myLinksViewModel.setFavorite(isFavorite = isFavorite, code = code)

            coVerify { updateFavoriteUseCase(isFavorite = isFavorite, code = code) }
        }

    @Test
    fun `Verify shorten data updated when shorten data selected with no old selected data`() =
        coroutineTestRule.runBlockingTest {
            val oldCode = null
            val code = "code"
            val isSelected = true
            coEvery { getSelectedOldUseCase() } returns oldCode

            myLinksViewModel.selectedShortenData(isSelected = isSelected, code = code)

            coVerify(exactly = 0) { updateShortenDataUseCase(isSelected = false, code = any()) }
            coVerify { updateShortenDataUseCase(isSelected = isSelected, code = code) }
        }

    @Test
    fun `Verify shorten data updated when shorten data selected with old selected data`() =
        coroutineTestRule.runBlockingTest {
            val oldCode = "oldCode"
            val code = "code"
            val isSelected = true
            coEvery { getSelectedOldUseCase() } returns oldCode

            myLinksViewModel.selectedShortenData(isSelected = isSelected, code = code)

            coVerify { updateShortenDataUseCase(isSelected = false, code = oldCode) }
            coVerify { updateShortenDataUseCase(isSelected = isSelected, code = code) }
        }

    @Test
    fun `Verify link deleted and error did not showed when link deleted`() =
        coroutineTestRule.runBlockingTest {
            val code = "code"
            val deferred = async { myLinksViewModel.uiEvent.first() }
            coEvery { deleteLinkUseCase(code) } returns true

            myLinksViewModel.delete(code = code)

            coVerify { deleteLinkUseCase(code = code) }
            assertFalse(deferred.isCompleted)
            deferred.cancel()
        }

    @Test
    fun `Verify error showed when link did not delete`() =
        coroutineTestRule.runBlockingTest {
            val code = "code"
            val deferred = async { myLinksViewModel.uiEvent.first() }
            coEvery { deleteLinkUseCase(code = code) } returns false

            myLinksViewModel.delete(code = code)

            coVerify { deleteLinkUseCase(code = code) }
            assertEquals(deferred.await(), ShowError)
        }
}