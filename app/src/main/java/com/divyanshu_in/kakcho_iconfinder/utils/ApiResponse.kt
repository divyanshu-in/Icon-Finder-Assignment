package com.divyanshu_in.kakcho_iconfinder.utils

sealed class ApiResponse<T>(
    val response: T? = null,
    val errorMessage: String? = null,
    val headers: okhttp3.Headers? = null,
) {
    class Success<T>(response: T, headers: okhttp3.Headers? = null) :
        ApiResponse<T>(response = response, headers = headers)

    class Error<T>(errorMessage: String, headers: okhttp3.Headers? = null) :
        ApiResponse<T>(errorMessage = errorMessage, headers = headers)
}