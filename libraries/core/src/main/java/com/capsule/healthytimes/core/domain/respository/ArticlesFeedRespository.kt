package com.capsule.healthytimes.core.domain.respository

import com.capsule.healthytimes.core.domain.model.ApiResult
import com.capsule.healthytimes.core.domain.model.ArticlesFeedList

interface ArticlesFeedRepository {
    suspend fun fetchArticles(pageNo: Int): ApiResult<ArticlesFeedList>
}