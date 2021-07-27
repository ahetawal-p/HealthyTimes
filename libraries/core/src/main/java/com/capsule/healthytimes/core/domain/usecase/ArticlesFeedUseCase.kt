package com.capsule.healthytimes.core.domain.usecase

import com.capsule.healthytimes.core.domain.respository.ArticlesFeedRepository
import javax.inject.Inject

/**
 * UseCase to provide article feed
 */
class ArticlesFeedUseCase @Inject constructor(
    private val repo: ArticlesFeedRepository
) {
    suspend operator fun invoke(pageNo: Int) =
        repo.fetchArticles(pageNo)
}