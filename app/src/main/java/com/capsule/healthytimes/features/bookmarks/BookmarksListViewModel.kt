package com.capsule.healthytimes.features.bookmarks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capsule.healthytimes.common.ArticleDisplay
import com.capsule.healthytimes.common.ViewState
import com.capsule.healthytimes.core.domain.model.ApiResult
import com.capsule.healthytimes.core.domain.usecase.AllBookmarkedArticleUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model for Bookmarks list
 */
class BookmarksListViewModel @Inject constructor(
    private val allFavsUseCase: AllBookmarkedArticleUseCase
) : ViewModel() {

    private val _data: MutableLiveData<ViewState<ArticleDisplay>> = MutableLiveData()
    val data: LiveData<ViewState<ArticleDisplay>> = _data


    fun fetchFavs() {
        _data.value = ViewState.Loading
        viewModelScope.launch {
            allFavsUseCase().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        val data = result.data
                        val newData = ArticleDisplay.from(data, data.totalResults)
                        _data.value = ViewState.Success(newData)
                    }
                    is ApiResult.Error -> {
                        _data.value = ViewState.Error(result)
                    }
                }
            }
        }
    }
}