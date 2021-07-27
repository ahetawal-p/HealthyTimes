package com.capsule.healthytimes.core.domain.usecase

import com.capsule.healthytimes.core.domain.model.ApiResult
import com.capsule.healthytimes.core.domain.respository.ArticlesFeedRepository
import com.capsule.healthytimes.core.util.TestCoroutineRule
import com.capsule.healthytimes.core.util.mockArticlesFeedList
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ArticlesFeedUseCaseTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var useCase: ArticlesFeedUseCase

    private val repo: ArticlesFeedRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        useCase = ArticlesFeedUseCase(repo)
    }

    @Test
    fun `Verify feed is fetched`() = testCoroutineRule.runBlockingTest {
        // Given
        coEvery {
            repo.fetchArticles(1)
        } returns ApiResult.Success(mockArticlesFeedList())

        // When
        val result = useCase.invoke(1)

        // Then
        val actualData = (result as ApiResult.Success).data
        Assertions.assertThat(actualData.feedList.size).isEqualTo(1)
        Assertions.assertThat(actualData.feedList[0].id).isEqualTo("test")
    }
}