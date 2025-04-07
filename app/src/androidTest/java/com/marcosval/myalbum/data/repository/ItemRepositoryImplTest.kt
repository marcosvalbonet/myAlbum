package com.marcosval.myalbum.data.repository

import com.marcosval.myalbum.data.local.ItemDao
import com.marcosval.myalbum.data.local.ItemEntity
import com.marcosval.myalbum.data.remote.ApiService
import com.marcosval.myalbum.domain.model.Item
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
class ItemRepositoryImplTest {

    private lateinit var repository: ItemRepositoryImpl
    private val apiService: ApiService = mock()
    private val itemDao: ItemDao = mock()

    @Before
    fun setUp() {
        repository = ItemRepositoryImpl(apiService, itemDao)
    }

    @Test
    fun fetchItemsFromApiAndSaveToRoom() = runTest {
        val mockItemsDto = listOf(Item(1, "Item 1", "Desc", "url", thumbnailUrl = "thumbnail"))
        val mockEntities = mockItemsDto.map { it.toEntity() }
        val mockDomainItems = mockItemsDto.map { it }

        whenever(apiService.getItems()).thenReturn(mockItemsDto)

        val result = repository.getItems()

        verify(itemDao).insertAll(mockEntities)
        assertEquals(mockDomainItems, result)
    }

    @Test
    fun fetchItemsFromRoomWhenApiFails() = runTest {
        val mockEntities = listOf(ItemEntity(1, "Item 1", "Desc", "url", thumbnailUrl = "thumbnail"))
        val mockDomainItems = mockEntities.map { it.toDomain() }

        whenever(apiService.getItems()).thenThrow(RuntimeException("API Error"))
        whenever(itemDao.getAllItems()).thenReturn(mockEntities)

        val result = repository.getItems()

        verify(itemDao, never()).insertAll(anyList())
        assertEquals(mockDomainItems, result)
    }
}
