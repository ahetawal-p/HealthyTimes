package com.capsule.healthytimes.core.util

import com.capsule.healthytimes.core.domain.model.Article
import com.capsule.healthytimes.core.domain.model.ArticlesFeedList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> flowableMockk(call: () -> T): Flow<T> = flow {
    emit(call())
}

fun mockArticlesFeedList(): ArticlesFeedList {
    return ArticlesFeedList(listOf(mockArticle()), 1)
}

fun mockArticle(id: String = "test", url: String = "testUrl"): Article {
    return Article(
        id = id,
        abstractInfo = "Some Info",
        url = url,
        headline = "Some headline",
        thumbnail = "some thumbnail",
        isFavorite = false
    )
}