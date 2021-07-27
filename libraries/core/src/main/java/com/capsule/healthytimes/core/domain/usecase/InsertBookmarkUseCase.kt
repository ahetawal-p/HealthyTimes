package com.capsule.healthytimes.core.domain.usecase

import com.capsule.healthytimes.core.domain.model.ApiResult
import com.capsule.healthytimes.core.domain.model.Article
import com.capsule.healthytimes.core.domain.respository.BookmarksRepository
import javax.inject.Inject

/**
 * UseCase to insert a new bookmark
 */
class InsertBookmarkUseCase @Inject constructor(
    private val repo: BookmarksRepository
) {
    suspend operator fun invoke(article: Article): ApiResult<Long> =
        repo.insertBookmark(article)
}