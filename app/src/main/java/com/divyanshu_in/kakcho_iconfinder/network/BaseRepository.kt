package com.divyanshu_in.kakcho_iconfinder.network

import com.divyanshu_in.kakcho_iconfinder.utils.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

abstract class BaseRepository {

    suspend fun <T> safeApiCall(apiToBeCalled: suspend () -> Response<T>): ApiResponse<T> {

        return withContext(Dispatchers.IO) {

            val response: Response<T> = apiToBeCalled.invoke()
            if (response.isSuccessful) {
                ApiResponse.Success(response = response.body()!!, response.headers())
            } else {
                ApiResponse.Error("Error!")
            }
        }
    }

}