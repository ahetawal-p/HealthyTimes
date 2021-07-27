package com.capsule.healthytimes.core.domain.usecase

import com.capsule.healthytimes.core.domain.model.Article
import com.capsule.healthytimes.core.domain.respository.BookmarksRepository
import javax.inject.Inject

/**
 * UseCase to find an article if its bookmarked
 */
class FindBookmarkUseCase @Inject constructor(
    private val repo: BookmarksRepository
) {
    suspend operator fun invoke(article: Article) = repo.findBookmark(article.id)
}