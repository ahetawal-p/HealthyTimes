package com.capsule.healthytimes.features.bookmarks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.capsule.healthytimes.common.ArticleDisplay
import com.capsule.healthytimes.common.ViewState
import com.capsule.healthytimes.core.domain.model.ApiResult
import com.capsule.healthytimes.core.domain.usecase.AllBookmarkedArticleUseCase
import com.capsule.healthytimes.util.TestCoroutineRule
import com.capsule.healthytimes.util.flowableMockk
import com.capsule.healthytimes.util.mockArticlesFeedList
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BookmarksListViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val allFavsUseCase: AllBookmarkedArticleUseCase = mockk()
    private val mockState: Observer<ViewState<ArticleDisplay>> = mockk(relaxed = true)
    private val testFeedList = mockArticlesFeedList()

    private lateinit var viewModel: BookmarksListViewModel

    @Before
    fun setUp() {
        viewModel = BookmarksListViewModel(
            allFavsUseCase
        ).apply {
            data.observeForever(mockState)
        }
    }

    @Test
    fun `Verify fetch favs`() = testCoroutineRule.runBlockingTest {

        coEvery { allFavsUseCase() } returns flowableMockk { ApiResult.Success(testFeedList) }

        // when
        viewModel.fetchFavs()

        // then
        verify {
            mockState.onChanged(ViewState.Loading)
            mockState.onChanged(
                ViewState.Success(ArticleDisplay.from(testFeedList, testFeedList.totalResults))
            )
        }
    }

}