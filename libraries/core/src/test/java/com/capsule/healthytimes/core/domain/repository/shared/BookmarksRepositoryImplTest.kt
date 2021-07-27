package com.capsule.healthytimes.core.domain.repository.shared

import com.capsule.healthytimes.core.db.BookmarkDao
import com.capsule.healthytimes.core.domain.model.ApiResult
import com.capsule.healthytimes.core.domain.respository.BookmarksRepository
import com.capsule.healthytimes.core.domain.respository.shared.BookmarksRepositoryImpl
import com.capsule.healthytimes.core.util.flowableMockk
import com.capsule.healthytimes.core.util.mockArticle
import com.capsule.healthytimes.core.util.mockArticlesFeedList
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class BookmarksRepositoryImplTest {

    private lateinit var repo: BookmarksRepository

    private val dao: BookmarkDao = mockk(relaxed = true)

    private val mockArticle = mockArticle()

    @Before
    fun setup() {
        repo = BookmarksRepositoryImpl(dao)
    }

    @Test
    fun `Verify all bookmarks are fetched`() {
        runBlocking {
            // Given
            coEvery {
                dao.getAllFavs()
            } returns flowableMockk { mockArticlesFeedList().feedList }

            // When
            val result = repo.getBookmarks().first()

            // Then
            val actualData = (result as ApiResult.Success).data
            assertThat(actualData.feedList.size).isEqualTo(1)
            assertThat(actualData.feedList[0].id).isEqualTo("test")
        }

    }

    @Test
    fun `Verify bookmark is deleted`() {
        runBlocking {
            // Given
            coEvery {
                dao.deleteFav(mockArticle)
            } returns Unit

            // When
            repo.deleteBookmark(mockArticle)
        }


    }

    @Test
    fun `Verify bookmark is inserted`() {
        runBlocking {
            val newId = 2L
            // Given
            coEvery {
                dao.insert(mockArticle)
            } returns newId

            // When
            val result = repo.insertBookmark(mockArticle)

            // Then
            val actualData = (result as ApiResult.Success).data
            assertThat(actualData).isEqualTo(newId)
        }


    }

    @Test
    fun `Verify find bookmark`() {
        runBlocking {
            // found
            // Given
            coEvery {
                dao.findArticle(mockArticle.id)
            } returns mockArticle

            // When
            val result = repo.findBookmark(mockArticle.id)

            // Then
            val actualData = (result as ApiResult.Success).data
            assertThat(actualData).isTrue()


            // not found
            coEvery {
                dao.findArticle(mockArticle.id)
            } returns null

            // When
            val newResult = repo.findBookmark(mockArticle.id)

            // Then
            val newActualData = (newResult as ApiResult.Success).data
            assertThat(newActualData).isFalse()
        }
    }
}