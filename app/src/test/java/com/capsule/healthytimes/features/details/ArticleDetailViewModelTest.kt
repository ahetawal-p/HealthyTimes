package com.capsule.healthytimes.features.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.capsule.healthytimes.common.ViewState
import com.capsule.healthytimes.core.domain.model.ApiResult
import com.capsule.healthytimes.core.domain.model.Article
import com.capsule.healthytimes.core.domain.usecase.DeleteBookmarkUseCase
import com.capsule.healthytimes.core.domain.usecase.FindBookmarkUseCase
import com.capsule.healthytimes.core.domain.usecase.InsertBookmarkUseCase
import com.capsule.healthytimes.util.TestCoroutineRule
import com.capsule.healthytimes.util.mockArticle
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ArticleDetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val insertUseCase: InsertBookmarkUseCase = mockk()
    private val deleteUseCase: DeleteBookmarkUseCase = mockk()
    private val findUseCase: FindBookmarkUseCase = mockk()

    private val mockLoadState: Observer<ViewState<ArticleDetailState>> = mockk(relaxed = true)
    private val testArticle: Article = mockArticle()

    private lateinit var viewModel: ArticleDetailViewModel

    @Before
    fun setUp() {
        viewModel = ArticleDetailViewModel(
            insertUseCase,
            deleteUseCase,
            findUseCase
        ).apply {
            loadState.observeForever(mockLoadState)
        }
    }

    @Test
    fun `Verify setup initial state for bookmark fragment`() = testCoroutineRule.runBlockingTest {
        // when
        viewModel.setupInitialState(testArticle, "bookmark")

        // then
        verify {
            mockLoadState.onChanged(
                ViewState.Success(ArticleDetailState(testArticle.url, true))
            )
        }
    }

    @Test
    fun `Verify setup initial state for feedlist fragment`() = testCoroutineRule.runBlockingTest {

        coEvery { findUseCase(any()) } returns ApiResult.Success(false)

        // when
        viewModel.setupInitialState(testArticle, "feed")

        // then
        verify {
            mockLoadState.onChanged(
                ViewState.Success(ArticleDetailState(testArticle.url, false))
            )
        }
    }

    @Test
    fun `Verify toggle favorite on`() = testCoroutineRule.runBlockingTest {

        // verify this is called
        coEvery { insertUseCase(any()) } returns ApiResult.Success(2L)

        // when
        viewModel.toggleFavorite(testArticle, true)
    }

    @Test
    fun `Verify toggle favorite off`() = testCoroutineRule.runBlockingTest {

        // verify this is called
        coEvery { deleteUseCase(any()) } returns Unit

        // when
        viewModel.toggleFavorite(testArticle, false)
    }

}