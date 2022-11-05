package com.divyanshu_in.kakcho_iconfinder.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ListCategoriesData(
    @Json(name = "categories")
    val categories: List<Category>,
    @Json(name = "total_count")
    val totalCount: Int
) {
    @JsonClass(generateAdapter = true)
    data class Category(
        @Json(name = "identifier")
        val identifier: String,
        @Json(name = "name")
        val name: String
    )
}