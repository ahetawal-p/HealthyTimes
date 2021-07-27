package com.capsule.healthytimes.features.feed.data

import com.capsule.healthytimes.core.domain.model.ApiResult
import com.capsule.healthytimes.core.domain.model.ArticlesFeedList
import com.capsule.healthytimes.core.domain.model.toDomain
import com.capsule.healthytimes.core.domain.respository.ArticleService
import com.capsule.healthytimes.core.domain.respository.ArticlesFeedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Data repository to make api calls to NYTimes api and fetch articles
 */
class ArticlesFeedRepositoryImpl @Inject constructor(private val articleService: ArticleService) :
    ArticlesFeedRepository {

    override suspend fun fetchArticles(pageNo: Int): ApiResult<ArticlesFeedList> {
        return withContext(Dispatchers.IO) {
            try {
                val response = articleService.query(page = pageNo)
                ApiResult.Success(response.toDomain())
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        ApiResult.Error(e.code(), e)
                    }
                    else -> {
                        ApiResult.Error(500, e)
                    }
                }
            }
        }
    }
}