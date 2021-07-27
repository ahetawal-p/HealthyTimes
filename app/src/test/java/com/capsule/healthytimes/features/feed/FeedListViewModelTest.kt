package com.capsule.healthytimes.features.feed

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.capsule.healthytimes.common.ArticleDisplay
import com.capsule.healthytimes.common.ViewState
import com.capsule.healthytimes.core.domain.model.ApiResult
import com.capsule.healthytimes.core.domain.usecase.ArticlesFeedUseCase
import com.capsule.healthytimes.util.TestCoroutineRule
import com.capsule.healthytimes.util.mockArticlesFeedList
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FeedListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val feedUseCase: ArticlesFeedUseCase = mockk()
    private val mockState: Observer<ViewState<ArticleDisplay>> = mockk(relaxed = true)
    private lateinit var viewModel: FeedListViewModel

    @Before
    fun setUp() {
        viewModel = FeedListViewModel(
            feedUseCase
        ).apply {
            data.observeForever(mockState)
        }
    }

    @Test
    fun `Fetch data page 1`() = testCoroutineRule.runBlockingTest {

        val testFeedList = mockArticlesFeedList()

        coEvery { feedUseCase(1) } returns ApiResult.Success(testFeedList)

        // when
        viewModel.fetchData(1)

        // then
        verify {
            mockState.onChanged(ViewState.Loading)
            mockState.onChanged(
                ViewState.Success(ArticleDisplay.from(testFeedList, testFeedList.totalResults))
            )
        }
    }

    @Test
    fun `Fetch data page 2`() = testCoroutineRule.runBlockingTest {

        val page1 = mockArticlesFeedList(totalResult = 2)
        val page1Display = ArticleDisplay.from(page1, 1)
        coEvery { feedUseCase(1) } returns ApiResult.Success(page1)
        // when
        viewModel.fetchData(1)
        // then
        verify {
            mockState.onChanged(ViewState.Loading)
            mockState.onChanged(
                ViewState.Success(page1Display)
            )
        }

        val page2 = mockArticlesFeedList("newId")
        val page2Display = ArticleDisplay.from(page2, page2.totalResults)
        val updatedData = viewModel.mergeData(page1Display, page2Display)
        coEvery { feedUseCase(2) } returns ApiResult.Success(page2)
        // when
        viewModel.fetchNextPage()
        // then
        verify {
            mockState.onChanged(
                ViewState.Success(updatedData)
            )
        }
    }

    @Test
    fun `Refresh feed`() = testCoroutineRule.runBlockingTest {

        val testFeedList = mockArticlesFeedList()

        coEvery { feedUseCase(1) } returns ApiResult.Success(testFeedList)

        // when
        viewModel.refreshFeed()

        // then
        verify {
            mockState.onChanged(ViewState.Loading)
            mockState.onChanged(
                ViewState.Success(ArticleDisplay.from(testFeedList, testFeedList.totalResults))
            )
        }
    }
}