package com.capsule.healthytimes.core.domain.usecase

import com.capsule.healthytimes.core.domain.model.ApiResult
import com.capsule.healthytimes.core.domain.respository.BookmarksRepository
import com.capsule.healthytimes.core.util.TestCoroutineRule
import com.capsule.healthytimes.core.util.mockArticle
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FindBookmarkUseCaseTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var useCase: FindBookmarkUseCase

    private val repo: BookmarksRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        useCase = FindBookmarkUseCase(repo)
    }

    @Test
    fun `Verify bookmark is found`() = testCoroutineRule.runBlockingTest {
        val mockArticle = mockArticle()
        // Given
        coEvery {
            repo.findBookmark(mockArticle.id)
        } returns ApiResult.Success(true)

        // make sure delete call is made
        val result = useCase(mockArticle)
        val actualData = (result as ApiResult.Success).data
        assertThat(actualData).isTrue()
    }

}