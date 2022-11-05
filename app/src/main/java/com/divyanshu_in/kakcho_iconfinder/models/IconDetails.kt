package com.divyanshu_in.kakcho_iconfinder.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IconDetails(
    @Json(name = "icon_id")
    val iconId: Int?,
    @Json(name = "iconset")
    val iconset: Iconset?,
    @Json(name = "is_premium")
    val isPremium: Boolean?,
    @Json(name = "raster_sizes")
    val rasterSizes: List<RasterSize?>?,
) {
    @JsonClass(generateAdapter = true)
    data class Category(
        @Json(name = "identifier")
        val identifier: String?,
        @Json(name = "name")
        val name: String?
    )

    @JsonClass(generateAdapter = true)
    data class Iconset(
        @Json(name = "author")
        val author: Author?,
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
        @Json(name = "website_url")
        val websiteUrl: String?
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
        data class License(
            @Json(name = "license_id")
            val licenseId: Int?,
            @Json(name = "name")
            val name: String?,
            @Json(name = "scope")
            val scope: String?
        )

    }

    @JsonClass(generateAdapter = true)
    data class RasterSize(
        @Json(name = "formats")
        val formats: List<Format?>?,
        @Json(name = "size")
        val size: Int?,
        @Json(name = "size_height")
        val sizeHeight: Int?,
        @Json(name = "size_width")
        val sizeWidth: Int?
    ) {
        @JsonClass(generateAdapter = true)
        data class Format(
            @Json(name = "download_url")
            val downloadUrl: String?,
            @Json(name = "format")
            val format: String?,
            @Json(name = "preview_url")
            val previewUrl: String?
        )
    }
}