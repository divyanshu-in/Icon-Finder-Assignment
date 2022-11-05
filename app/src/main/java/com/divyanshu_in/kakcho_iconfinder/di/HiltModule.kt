package com.divyanshu_in.kakcho_iconfinder.di

import android.content.Context
import com.divyanshu_in.kakcho_iconfinder.R
import com.divyanshu_in.kakcho_iconfinder.network.ApiBuilder
import com.divyanshu_in.kakcho_iconfinder.network.IconsApi
import com.divyanshu_in.kakcho_iconfinder.network.IconsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class HiltModule {

    @Provides
    @Singleton
    fun provideIconFinderApi(@ApplicationContext context: Context) =
        ApiBuilder(context.getString(R.string.API_KEY)).getIconFinderApi()

    @Provides
    @Singleton
    fun provideIconsRepository(iconsApi: IconsApi) = IconsRepository(iconsApi)

}