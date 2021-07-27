package com.capsule.healthytimes.core.domain.respository

import com.capsule.healthytimes.core.domain.model.ApiResult
import com.capsule.healthytimes.core.domain.model.Article
import com.capsule.healthytimes.core.domain.model.ArticlesFeedList
import kotlinx.coroutines.flow.Flow

interface BookmarksRepository {
    suspend fun getBookmarks(): Flow<ApiResult<ArticlesFeedList>>
    suspend fun deleteBookmark(article: Article)
    suspend fun insertBookmark(article: Article): ApiResult<Long>
    suspend fun findBookmark(articleId: String): ApiResult<Boolean>
}