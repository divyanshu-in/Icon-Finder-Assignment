package com.divyanshu_in.kakcho_iconfinder.ui

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.FileUtils
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divyanshu_in.kakcho_iconfinder.models.IconDetails
import com.divyanshu_in.kakcho_iconfinder.models.ListAllIconsInIconSetData
import com.divyanshu_in.kakcho_iconfinder.models.ListCategoriesData
import com.divyanshu_in.kakcho_iconfinder.models.ListItemSetsInCategoryData
import com.divyanshu_in.kakcho_iconfinder.network.IconsRepository
import com.divyanshu_in.kakcho_iconfinder.utils.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import java.io.OutputStream
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(val iconsRepo: IconsRepository): ViewModel() {

    var categoriesList = MutableStateFlow(emptyList<ListCategoriesData.Category>())

    var iconsetsList = MutableStateFlow(emptyList<ListItemSetsInCategoryData.Iconset?>())

    var iconsList = MutableStateFlow(emptyList<ListAllIconsInIconSetData.Icon?>())

    var searchIconList = MutableStateFlow(emptyList<ListAllIconsInIconSetData.Icon?>())

    var iconDetails: MutableStateFlow<IconDetails?> = MutableStateFlow(null)


    fun getCategories(){


        viewModelScope.launch {

            iconsRepo.listAllCategories(null).collect {

                if(it is ApiResponse.Success<*>){
                    categoriesList.emit(it.response?.categories ?: emptyList())
                }

            }
        }
    }

    fun getIconSets(categoryIdentifier: String, offset: Int){

        viewModelScope.launch {

            iconsRepo.getItemSets(categoryIdentifier, offset).collect {

                if(it is ApiResponse.Success<*>){
                    iconsetsList.emit(it.response?.iconsets ?: emptyList())
                }

            }
        }

    }

    fun getIcons(iconSetId: Int, offset: Int){

        viewModelScope.launch {

            iconsRepo.getAllIcons(iconSetId, offset).collect {

                if(it is ApiResponse.Success<*>){
                    iconsList.emit(it.response?.icons ?: emptyList())
                }
            }
        }
    }

    fun getIconsForSearch(query: String, offset: Int){

        viewModelScope.launch {

            iconsRepo.getIconsForSearch(query, offset).collect {
                if(it is ApiResponse.Success<*>){
                    searchIconList.emit(it.response?.icons ?: emptyList())
                }

            }

        }
    }

    fun getIconDetails(iconId: Int){
        viewModelScope.launch {

            iconsRepo.getIconDetails(iconId).collect {
                if(it is ApiResponse.Success<*>){
                    iconDetails.emit(it.response)
                }

            }

        }
    }

    fun downloadIcon(downloadUrl: String, context: Context){


        viewModelScope.launch(Dispatchers.IO) {

            iconsRepo.downloadIcon(downloadUrl).collect{ response ->
                if(response is ApiResponse.Success<*>){


                    response.response?.bytes()?.let {byteArray ->
                        response.headers?.get("Content-Disposition")?.split("filename=")?.last()?.let {
                            saveImage(byteArray, context, it)
                        }

                    }

                }
            }
        }
    }

    fun saveImage(
        byteArray: ByteArray,
        context: Context, fileName: String,
    ) {

        val outputStream: OutputStream?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS + File.separator.toString() + "Icons")

            val uri =
                resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            try {
                uri?.let {
                    outputStream = resolver.openOutputStream(it)

                    outputStream?.write(byteArray)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }else{
            val dir = File(context.filesDir, "Icons")
            //make directory icons if not available

            if (!dir.exists()) {
                dir.mkdir()
            }

            try {
                val file = File(dir, fileName)
                file.writeBytes(byteArray)

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }


}