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
class InsertBookmarkUseCaseTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var useCase: InsertBookmarkUseCase

    private val repo: BookmarksRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        useCase = InsertBookmarkUseCase(repo)
    }

    @Test
    fun `Verify bookmark is inserted`() = testCoroutineRule.runBlockingTest {
        val mockArticle = mockArticle()
        val newId = 2L
        // Given
        coEvery {
            repo.insertBookmark(mockArticle)
        } returns ApiResult.Success(newId)

        // When
        val result = useCase(mockArticle)

        // Then
        val actualData = (result as ApiResult.Success).data
        assertThat(actualData).isEqualTo(newId)

    }
}