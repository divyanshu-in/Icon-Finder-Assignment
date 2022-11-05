package com.divyanshu_in.kakcho_iconfinder.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ListItemSetsInCategoryData(
    @Json(name = "iconsets")
    val iconsets: List<Iconset?>?,
    @Json(name = "total_count")
    val totalCount: Int?
) {
    @JsonClass(generateAdapter = true)
    data class Iconset(
        @Json(name = "are_all_icons_glyph")
        val areAllIconsGlyph: Boolean?,
        @Json(name = "author")
        val author: Author?,
        @Json(name = "categories")
        val categories: List<Category?>?,
        @Json(name = "icons_count")
        val iconsCount: Int?,
        @Json(name = "iconset_id")
        val iconsetId: Int?,
        @Json(name = "identifier")
        val identifier: String?,
        @Json(name = "is_premium")
        val isPremium: Boolean?,
        @Json(name = "name")
        val name: String?,
        @Json(name = "prices")
        val prices: List<Price?>?,
        @Json(name = "published_at")
        val publishedAt: String?,
        @Json(name = "styles")
        val styles: List<Style?>?,
        @Json(name = "type")
        val type: String?
    ) {
        @JsonClass(generateAdapter = true)
        data class Author(
            @Json(name = "company")
            val company: String?,
            @Json(name = "iconsets_count")
            val iconsetsCount: Int?,
            @Json(name = "is_designer")
            val isDesigner: Boolean?,
            @Json(name = "name")
            val name: String?,
            @Json(name = "user_id")
            val userId: Int?,
            @Json(name = "username")
            val username: String?
        )

        @JsonClass(generateAdapter = true)
        data class Category(
            @Json(name = "identifier")
            val identifier: String?,
            @Json(name = "name")
            val name: String?
        )

        @JsonClass(generateAdapter = true)
        data class Price(
            @Json(name = "currency")
            val currency: String?,
            @Json(name = "license")
            val license: License?,
            @Json(name = "price")
            val price: Int?
        ) {
            @JsonClass(generateAdapter = true)
            data class License(
                @Json(name = "license_id")
                val licenseId: Int?,
                @Json(name = "name")
                val name: String?,
                @Json(name = "scope")
                val scope: String?,
                @Json(name = "url")
                val url: String?
            )
        }

        @JsonClass(generateAdapter = true)
        data class Style(
            @Json(name = "identifier")
            val identifier: String?,
            @Json(name = "name")
            val name: String?
        )
    }
}