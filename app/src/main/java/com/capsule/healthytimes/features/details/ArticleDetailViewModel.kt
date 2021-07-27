package com.capsule.healthytimes.features.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capsule.healthytimes.common.ViewState
import com.capsule.healthytimes.core.domain.model.ApiResult
import com.capsule.healthytimes.core.domain.model.Article
import com.capsule.healthytimes.core.domain.usecase.DeleteBookmarkUseCase
import com.capsule.healthytimes.core.domain.usecase.FindBookmarkUseCase
import com.capsule.healthytimes.core.domain.usecase.InsertBookmarkUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Article Detail fragment
 */
class ArticleDetailViewModel @Inject constructor(
    private val insertBookmarkUseCase: InsertBookmarkUseCase,
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase,
    private val findBookmarkUseCase: FindBookmarkUseCase,
) : ViewModel() {
    private val _loadState: MutableLiveData<ViewState<ArticleDetailState>> = MutableLiveData()
    val loadState: LiveData<ViewState<ArticleDetailState>> = _loadState

    fun setupInitialState(article: Article, source: String) {
        if (source == "bookmark") {
            _loadState.value = ViewState.Success(ArticleDetailState(article.url, true))
        } else {
            fetchFavStatus(article)
        }
    }

    fun toggleFavorite(article: Article, newFavStatus: Boolean) {
        viewModelScope.launch {
            val newArticle = article.copy(isFavorite = true)
            if (newFavStatus) {
                insertBookmarkUseCase(newArticle)
            } else {
                deleteBookmarkUseCase(article)
            }
        }
    }

    private fun fetchFavStatus(article: Article) {
        viewModelScope.launch {
            when (val result = findBookmarkUseCase(article)) {
                is ApiResult.Success -> {
                    val found = result.data
                    _loadState.value = ViewState.Success(ArticleDetailState(article.url, found))
                }
                else -> {
                    // do nothing
                }
            }
        }
    }
}

data class ArticleDetailState(val url: String, val isFav: Boolean)