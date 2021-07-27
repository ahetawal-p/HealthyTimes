package com.capsule.healthytimes.core.domain.usecase

import com.capsule.healthytimes.core.domain.respository.BookmarksRepository
import javax.inject.Inject

/**
 * UseCase to provide flowable latest bookmarked articles at any given time
 */
class AllBookmarkedArticleUseCase @Inject constructor(
    private val repo: BookmarksRepository
) {
    suspend operator fun invoke() = repo.getBookmarks()
}