package com.divyanshu_in.kakcho_iconfinder.network

import com.divyanshu_in.kakcho_iconfinder.models.IconDetails
import com.divyanshu_in.kakcho_iconfinder.models.ListAllIconsInIconSetData
import com.divyanshu_in.kakcho_iconfinder.models.ListCategoriesData
import com.divyanshu_in.kakcho_iconfinder.models.ListItemSetsInCategoryData
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


interface IconsApi {

    @GET("categories")
    suspend fun listAllCategories(
        @Query("after") lastCategoryIdentifier: String?,
        @Query("count") count: Int = 25,
    ): Response<ListCategoriesData>

    @GET("categories/{category_identifier}/iconsets")
    suspend fun getItemSets(
        @Path("category_identifier") category: String,
        @Query("offset") offset: Int,
        @Query("count") count: Int = 25,
    ): Response<ListItemSetsInCategoryData>

    @GET("iconsets/{iconset_id}/icons")
    suspend fun getAllIcons(
        @Path("iconset_id") iconsetId: Int,
        @Query("offset") offset: Int,
        @Query("count") count: Int = 25,
    ): Response<ListAllIconsInIconSetData>

    @GET("icons/search")
    suspend fun getIconsForSearch(
        @Query("query") searchQuery: String,
        @Query("offset") offset: Int,
        @Query("count") count: Int = 25,
    ): Response<ListAllIconsInIconSetData>

    @GET("icons/{icon_id}")
    suspend fun getIconDetails(
        @Path("icon_id") iconId: Int,
    ): Response<IconDetails>


    @GET
    suspend fun downloadIcon(
        @Url downloadUrl: String,
    ): Response<ResponseBody>


}