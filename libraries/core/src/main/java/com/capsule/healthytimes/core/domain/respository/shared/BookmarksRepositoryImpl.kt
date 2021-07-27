package com.capsule.healthytimes.core.domain.respository.shared

import com.capsule.healthytimes.core.db.BookmarkDao
import com.capsule.healthytimes.core.domain.model.ApiResult
import com.capsule.healthytimes.core.domain.model.Article
import com.capsule.healthytimes.core.domain.model.ArticlesFeedList
import com.capsule.healthytimes.core.domain.respository.BookmarksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data repository for getting bookmark information for articles
 */
@Singleton
class BookmarksRepositoryImpl @Inject constructor(private val dao: BookmarkDao) :
    BookmarksRepository {
    override suspend fun getBookmarks(): Flow<ApiResult<ArticlesFeedList>> {
        return flow {
            try {
                val resultFlow = dao.getAllFavs()
                resultFlow.collect { result ->
                    emit(ApiResult.Success(ArticlesFeedList(result, result.size)))
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(500, e))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun deleteBookmark(article: Article) {
        return withContext(Dispatchers.IO) {
            try {
                dao.deleteFav(article)
            } catch (e: Exception) {
            }
        }
    }

    override suspend fun insertBookmark(article: Article): ApiResult<Long> {
        return withContext(Dispatchers.IO) {
            try {
                val value = dao.insert(article)
                ApiResult.Success(value)
            } catch (e: Exception) {
                ApiResult.Error(500, e)
            }
        }
    }

    override suspend fun findBookmark(articleId: String): ApiResult<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val value = dao.findArticle(articleId)
                if (value != null) {
                    ApiResult.Success(true)
                } else {
                    ApiResult.Success(false)
                }

            } catch (e: Exception) {
                ApiResult.Error(500, e)
            }
        }
    }
}