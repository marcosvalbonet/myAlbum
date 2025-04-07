package com.marcosval.myalbum.data.di

import android.content.Context
import androidx.room.Room
import com.marcosval.myalbum.data.local.AppDatabase
import com.marcosval.myalbum.data.local.ItemDao
import com.marcosval.myalbum.data.remote.ApiService
import com.marcosval.myalbum.data.repository.ItemRepositoryImpl
import com.marcosval.myalbum.domain.repository.ItemRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {

    private const val BASE_URL = "https://static.leboncoin.fr/"

    @Module
    @InstallIn(SingletonComponent::class)
    object AppModule {

        @Provides
        @Singleton
        fun provideApiService(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "app_database"
            ).build()
        }

        @Provides
        fun provideItemDao(db: AppDatabase): ItemDao = db.itemDao()

        @Provides
        @Singleton
        fun provideItemRepository(apiService: ApiService, itemDao: ItemDao): ItemRepository {
            return ItemRepositoryImpl(apiService, itemDao)
        }
    }

}