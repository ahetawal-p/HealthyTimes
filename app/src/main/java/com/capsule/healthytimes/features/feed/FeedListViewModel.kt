package com.capsule.healthytimes.features.feed

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capsule.healthytimes.common.ArticleDisplay
import com.capsule.healthytimes.common.ViewState
import com.capsule.healthytimes.common.extractSuccessData
import com.capsule.healthytimes.core.domain.model.ApiResult
import com.capsule.healthytimes.core.domain.model.Article
import com.capsule.healthytimes.core.domain.usecase.ArticlesFeedUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View Model for feed list
 */
class FeedListViewModel @Inject constructor(
    private val useCase: ArticlesFeedUseCase
) : ViewModel() {

    private val _data: MutableLiveData<ViewState<ArticleDisplay>> = MutableLiveData()
    val data: LiveData<ViewState<ArticleDisplay>> = _data

    private var currentPage = 1
    private var totalResults = -1
    private var resultCount = 0


    fun fetchData(pageNo: Int = 1) {
        val previousData = data.value?.extractSuccessData()
        viewModelScope.launch {
            val result = useCase(pageNo)
            if (currentPage == 1) {
                _data.value = ViewState.Loading
            }
            when (result) {
                is ApiResult.Success -> {
                    val data = result.data
                    resultCount += data.feedList.size
                    totalResults = data.totalResults
                    var newData = ArticleDisplay.from(data, resultCount)
                    if (currentPage != 1 && previousData != null) {
                        newData = mergeData(previousData, newData)
                    }
                    _data.value = ViewState.Success(newData)
                    ++currentPage
                }
                is ApiResult.Error -> {
                    _data.value = ViewState.Error(result)
                }
            }
        }

    }

    fun fetchNextPage() {
        if (resultCount == totalResults) {
            return
        }
        fetchData(currentPage)
    }

    fun refreshFeed() {
        currentPage = 1
        totalResults = -1
        resultCount = 0
        fetchData(1)
    }

    @VisibleForTesting
    fun mergeData(
        prevData: ArticleDisplay,
        newData: ArticleDisplay
    ): ArticleDisplay {
        val updatedList = mutableListOf<Article>()
        updatedList.addAll(prevData.results)
        updatedList.addAll(newData.results)
        return ArticleDisplay(updatedList, newData.hasMore)
    }
}