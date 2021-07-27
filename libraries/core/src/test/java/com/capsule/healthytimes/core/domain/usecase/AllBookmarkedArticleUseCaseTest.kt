package com.capsule.healthytimes.core.domain.usecase

import com.capsule.healthytimes.core.domain.model.ApiResult
import com.capsule.healthytimes.core.domain.respository.BookmarksRepository
import com.capsule.healthytimes.core.util.TestCoroutineRule
import com.capsule.healthytimes.core.util.flowableMockk
import com.capsule.healthytimes.core.util.mockArticlesFeedList
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AllBookmarkedArticleUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var useCase: AllBookmarkedArticleUseCase

    private val repo: BookmarksRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        useCase = AllBookmarkedArticleUseCase(repo)
    }

    @Test
    fun `Verify all bookmarks are fetched`() = testCoroutineRule.runBlockingTest {
        // Given
        coEvery {
            repo.getBookmarks()
        } returns flowableMockk { ApiResult.Success(mockArticlesFeedList()) }

        // When
        val result = useCase().first()

        // Then
        val actualData = (result as ApiResult.Success).data
        assertThat(actualData.feedList.size).isEqualTo(1)
        assertThat(actualData.feedList[0].id).isEqualTo("test")
    }

}