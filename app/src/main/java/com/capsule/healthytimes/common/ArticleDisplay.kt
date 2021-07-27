package com.capsule.healthytimes.common

import com.capsule.healthytimes.core.domain.model.Article
import com.capsule.healthytimes.core.domain.model.ArticlesFeedList

data class ArticleDisplay(
    val results: List<Article>,
    val hasMore: Boolean
) {
    companion object {
        fun from(
            payload: ArticlesFeedList,
            currentCount: Int
        ): ArticleDisplay {
            val resultList = payload.feedList
            val totalResults = payload.totalResults
            if (currentCount < totalResults) {
                return ArticleDisplay(resultList, true)
            }
            return ArticleDisplay(resultList, false)
        }
    }
}