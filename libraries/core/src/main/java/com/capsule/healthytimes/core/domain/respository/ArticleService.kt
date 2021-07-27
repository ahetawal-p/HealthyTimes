package com.capsule.healthytimes.core.domain.respository

import com.capsule.healthytimes.core.domain.model.Articles
import com.capsule.healthytimes.core.domain.model.ArticlesFeedList
import retrofit2.Retrofit

class ArticleService(private val retrofit: Retrofit) : ArticlesApi {
    private val articlesApi by lazy { retrofit.create(ArticlesApi::class.java) }

    override suspend fun query(
        filteredFields: String,
        filteredQuery: String,
        sort: String,
        page: Int
    ): Articles {
        return articlesApi.query(filteredFields, filteredQuery, sort, page)
    }
}