package com.capsule.healthytimes.core.domain.model

/**
 * A generic class that holds a result value from any api call
 * @param <T>
 */
sealed class ApiResult<out T> {

    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(
        val errorCode: Int? = null,
        val exception: Exception? = null
    ) : ApiResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[core=$errorCode exception=$exception]"
        }
    }
}