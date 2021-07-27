package com.capsule.healthytimes.common

import com.capsule.healthytimes.core.domain.model.ApiResult

sealed class ViewState<out T> {

    data class Success<out T>(val data: T) : ViewState<T>()
    data class Error<out T>(val error: ApiResult.Error, val previousData: T? = null) :
        ViewState<T>()

    object Loading : ViewState<Nothing>() {
        override fun toString() = "Loading"
    }

    // implement this class for adding any custom states needed for a view
    abstract class Custom(val data: Any? = null) : ViewState<Nothing>() {
        override fun toString() = "Custom[customdata=$data]"
    }
}

fun <T> ViewState<T>.extractSuccessData(): T? {
    return when (this) {
        is ViewState.Success<T> -> data
        is ViewState.Error<T> -> previousData
        else -> null
    }
}