package com.divyanshu_in.kakcho_iconfinder.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ListAllIconsInIconSetData(
    @Json(name = "icons")
    val icons: List<Icon>,
    @Json(name = "total_count")
    val totalCount: Int
) {
    @JsonClass(generateAdapter = true)
    data class Icon(
        @Json(name = "categories")
        val categories: List<Category>,
        @Json(name = "containers")
        val containers: List<Container>,
        @Json(name = "icon_id")
        val iconId: Int,
        @Json(name = "is_icon_glyph")
        val isIconGlyph: Boolean,
        @Json(name = "is_premium")
        val isPremium: Boolean,
        @Json(name = "published_at")
        val publishedAt: String,
        @Json(name = "raster_sizes")
        val rasterSizes: List<RasterSize>,
        @Json(name = "styles")
        val styles: List<Style>,
        @Json(name = "tags")
        val tags: List<String>,
        @Json(name = "type")
        val type: String,
        @Json(name = "vector_sizes")
        val vectorSizes: List<VectorSize>
    ) {
        @JsonClass(generateAdapter = true)
        data class Category(
            @Json(name = "identifier")
            val identifier: String,
            @Json(name = "name")
            val name: String
        )

        @JsonClass(generateAdapter = true)
        data class Container(
            @Json(name = "download_url")
            val downloadUrl: String,
            @Json(name = "format")
            val format: String
        )

        @JsonClass(generateAdapter = true)
        data class RasterSize(
            @Json(name = "formats")
            val formats: List<Format>,
            @Json(name = "size")
            val size: Int,
            @Json(name = "size_height")
            val sizeHeight: Int,
            @Json(name = "size_width")
            val sizeWidth: Int
        ) {
            @JsonClass(generateAdapter = true)
            data class Format(
                @Json(name = "download_url")
                val downloadUrl: String,
                @Json(name = "format")
                val format: String,
                @Json(name = "preview_url")
                val previewUrl: String
            )
        }

        @JsonClass(generateAdapter = true)
        data class Style(
            @Json(name = "identifier")
            val identifier: String,
            @Json(name = "name")
            val name: String
        )

        @JsonClass(generateAdapter = true)
        data class VectorSize(
            @Json(name = "formats")
            val formats: List<Format>,
            @Json(name = "size")
            val size: Int,
            @Json(name = "size_height")
            val sizeHeight: Int,
            @Json(name = "size_width")
            val sizeWidth: Int,
            @Json(name = "target_sizes")
            val targetSizes: List<List<Int>>
        ) {
            @JsonClass(generateAdapter = true)
            data class Format(
                @Json(name = "download_url")
                val downloadUrl: String,
                @Json(name = "format")
                val format: String
            )
        }
    }
}