package com.capsule.healthytimes.features.feed.data

import com.capsule.healthytimes.core.domain.model.ApiResult
import com.capsule.healthytimes.core.domain.respository.ArticleService
import com.capsule.healthytimes.core.domain.respository.ArticlesFeedRepository
import com.capsule.healthytimes.util.getSpyedMockApiService
import com.capsule.healthytimes.util.mockResponse
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ArticlesFeedRepositoryImplTest {

    private lateinit var service: ArticleService

    private lateinit var repo: ArticlesFeedRepository

    @get:Rule
    val server = MockWebServer()

    companion object {
        private const val API_RESPONSE = "nytimes_response.json"
    }

    @Before
    fun setup() {
        service = getSpyedMockApiService(server.url("/"))
        repo = ArticlesFeedRepositoryImpl(service)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `Verify article data fetch`() {
        server.enqueue(mockResponse(API_RESPONSE, this::class.java))
        runBlocking {
            val result = repo.fetchArticles(1)

            // verify correct query call with params is made
            coVerify {
                service.query(
                    "docs,headline,multimedia,abstract,web_url,_id,meta",
                    "headline:(\"Health\" \"Healthy\" \"Exercise\" \"Diet\")",
                    "newest",
                    1
                )
            }
            val actualData = (result as ApiResult.Success).data
            
            // verify 4 articles are returned and parsed correctly
            assertThat(actualData.feedList.size).isEqualTo(4)
            assertThat(actualData.feedList[0].id).isEqualTo("nyt://article/78f1063c-bfb4-599d-9558-25d34a3f44bc")
            assertThat(actualData.feedList[0].url).isEqualTo("https://www.nytimes.com/2021/07/22/us/la-county-mask-mandate.html")

        }
    }
}