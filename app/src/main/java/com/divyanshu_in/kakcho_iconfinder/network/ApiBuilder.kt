package com.divyanshu_in.kakcho_iconfinder.network

import com.divyanshu_in.kakcho_iconfinder.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class ApiBuilder(private val apiKey: String) {

    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    fun getIconFinderApi(): IconsApi {

        val client = OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor {
                val originalRequest: Request = it.request()

                val newRequest = originalRequest.newBuilder().apply {
                    addHeader("accept", "application/json")
                    addHeader("Authorization", "Bearer $apiKey")
                }.build()

                return@addInterceptor it.proceed(newRequest)
            }
            .addNetworkInterceptor(httpLoggingInterceptor)
            .build()

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val retrofit = Retrofit
            .Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi)).client(client).baseUrl(BASE_URL)
            .build()

        return retrofit.create(IconsApi::class.java)
    }



}