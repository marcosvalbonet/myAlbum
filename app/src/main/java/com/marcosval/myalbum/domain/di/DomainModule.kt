package com.marcosval.myalbum.domain.di

import com.marcosval.myalbum.domain.repository.ItemRepository
import com.marcosval.myalbum.domain.usecase.GetItemsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Provides
    @Singleton
    fun provideListUseCase(itemRepository: ItemRepository) =
        GetItemsUseCase(itemRepository)

}