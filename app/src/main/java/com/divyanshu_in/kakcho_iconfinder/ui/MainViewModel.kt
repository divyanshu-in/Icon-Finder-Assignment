package com.divyanshu_in.kakcho_iconfinder.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divyanshu_in.kakcho_iconfinder.R
import com.divyanshu_in.kakcho_iconfinder.models.*
import com.divyanshu_in.kakcho_iconfinder.network.IconsRepository
import com.divyanshu_in.kakcho_iconfinder.utils.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.OutputStream
import javax.inject.Inject
import kotlin.random.Random


const val CHANNEL_ID = "ICON_FINDER_101"

@HiltViewModel
class MainViewModel @Inject constructor(val iconsRepo: IconsRepository): ViewModel() {

    var categoryData: MutableStateFlow<ListCategoriesData?> = MutableStateFlow(null)

    var iconsetsList: MutableStateFlow<ListItemSetsInCategoryData?> = MutableStateFlow(null)

    var iconsList = MutableStateFlow(emptyList<ListAllIconsInIconSetData.Icon?>())

    var searchIconData: MutableStateFlow<ListAllIconsInIconSetData?> = MutableStateFlow(null)

    var iconDetails: MutableStateFlow<IconDetails?> = MutableStateFlow(null)

    var downloadStatus: MutableStateFlow<Status> = MutableStateFlow(Status.LOADING)


    fun getCategories(lastCategoryIdentifier: String?){


        viewModelScope.launch {

            iconsRepo.listAllCategories(lastCategoryIdentifier).collect {

                if(it is ApiResponse.Success<*>){
                    categoryData.emit(it.response)
                }

            }
        }
    }

    suspend fun getIconSets(categoryIdentifier: String, offset: Int){



            iconsRepo.getItemSets(categoryIdentifier, offset).collect {

                if(it is ApiResponse.Success<*>){
                    iconsetsList.emit(it.response)
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

    suspend fun getIconsForSearch(query: String, offset: Int){

            iconsRepo.getIconsForSearch(query, offset).collect {
                if(it is ApiResponse.Success<*>){
                    searchIconData.emit(it.response)
                }
            }
    }

    private fun createNotificationChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotificationForDownloadedIcon(fileName: String, context: Context){
        var builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_premium)
            .setContentTitle("Icon Downloaded \uD83D\uDC4D")
            .setContentText("$fileName added to the downloads Successfully!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            val notificationId = Random.nextInt()
            notify(notificationId, builder.build())
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
            downloadStatus.emit(Status.LOADING)

            iconsRepo.downloadIcon(downloadUrl).collect{ response ->
                if(response is ApiResponse.Success<*>){


                    response.response?.bytes()?.let {byteArray ->
                        response.headers?.get("Content-Disposition")?.split("filename=")?.last()?.let { fileName ->
                            val imageSaved = saveImage(byteArray, context, fileName)

                            if(imageSaved){
                                downloadStatus.emit(Status.FINISHED)
                                createNotificationChannel(context)
                                showNotificationForDownloadedIcon(fileName, context)
                            }else{
                                downloadStatus.emit(Status.FAILED)
                            }
                        }

                    }

                }
            }
        }
    }

    fun saveImage(
        byteArray: ByteArray,
        context: Context, fileName: String,
    ): Boolean {

        val outputStream: OutputStream?


        //Using MediaStore api for android versions > Q
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
                    outputStream?.close()
                    return true
                }

            } catch (e: Exception) {
                e.printStackTrace()
                return false
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

                return true
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return false
        }
        return false
    }


}