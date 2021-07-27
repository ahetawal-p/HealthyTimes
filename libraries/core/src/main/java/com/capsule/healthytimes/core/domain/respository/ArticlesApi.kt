package com.capsule.healthytimes.core.domain.respository

import com.capsule.healthytimes.core.domain.model.Articles
import retrofit2.http.GET
import retrofit2.http.Query


interface ArticlesApi {

    @GET("articlesearch.json")
    suspend fun query(
        @Query("fl") filteredFields: String = "docs,headline,multimedia,abstract,web_url,_id,meta",
        //  @Query("fq") filteredQuery: String = "news_desk:(\"Health & Fitness\" \"Health\") OR headline:(\"Health\" \"Healthy\")",
        @Query("fq") filteredQuery: String = "headline:(\"Health\" \"Healthy\" \"Exercise\" \"Diet\")",
        @Query("sort") sort: String = "newest",
        @Query("page") page: Int = 1
    ): Articles
}