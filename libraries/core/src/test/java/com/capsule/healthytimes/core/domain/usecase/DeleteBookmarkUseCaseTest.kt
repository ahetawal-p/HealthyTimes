package com.capsule.healthytimes.core.domain.usecase

import com.capsule.healthytimes.core.domain.respository.BookmarksRepository
import com.capsule.healthytimes.core.util.TestCoroutineRule
import com.capsule.healthytimes.core.util.mockArticle
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DeleteBookmarkUseCaseTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var useCase: DeleteBookmarkUseCase

    private val repo: BookmarksRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        useCase = DeleteBookmarkUseCase(repo)
    }

    @Test
    fun `Verify delete call is executed`() = testCoroutineRule.runBlockingTest {
        val mockArticle = mockArticle()
        // Given
        coEvery {
            repo.deleteBookmark(mockArticle)
        } returns Unit

        // make sure delete call is made
        useCase(mockArticle)
    }

}