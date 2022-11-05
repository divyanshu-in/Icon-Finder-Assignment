package com.divyanshu_in.kakcho_iconfinder.network

import android.util.Log
import com.divyanshu_in.kakcho_iconfinder.models.ListCategoriesData
import com.divyanshu_in.kakcho_iconfinder.utils.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.Query
import javax.inject.Inject

class IconsRepository(val iconsApi: IconsApi): BaseRepository() {

    fun listAllCategories(lastCategoryIdentifier: String?) = flow{

        emit(
            safeApiCall {
                iconsApi.listAllCategories(lastCategoryIdentifier)
            }
        )
    }

    suspend fun getItemSets(categoryIdentifier: String, offset: Int) = flow{
        emit(
            safeApiCall {
                iconsApi.getItemSets(category = categoryIdentifier, offset)
            }
        )
    }

    fun getAllIcons(
        iconsetId: Int,
        offset: Int
    ) = flow{
        emit(
            safeApiCall { iconsApi.getAllIcons(iconsetId, offset) }
        )
    }

    fun getIconsForSearch(
        searchQuery: String,
        offset: Int
    ) = flow{

        emit(safeApiCall {
            iconsApi.getIconsForSearch(searchQuery, offset)
        })

    }

    fun getIconDetails(
        iconId: Int
    ) = flow{

        emit(safeApiCall {
            iconsApi.getIconDetails(iconId)
        })

    }


    fun downloadIcon(
        downloadUrl: String
    ) = flow{

        emit(safeApiCall {
            iconsApi.downloadIcon(downloadUrl)
        })

    }

}