package com.marcosval.myalbum.data.repository

import com.marcosval.myalbum.data.local.ItemDao
import com.marcosval.myalbum.data.local.ItemEntity
import com.marcosval.myalbum.data.remote.ApiService
import com.marcosval.myalbum.domain.model.Item
import com.marcosval.myalbum.domain.repository.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItemRepositoryImpl(
    private val apiService: ApiService,
    private val itemDao: ItemDao
) : ItemRepository {

    override suspend fun getItems(): List<Item> {
        return withContext(Dispatchers.IO) {
            try {
                val items = apiService.getItems()
                itemDao.insertAll(items.map { it.toEntity() })
                items
            } catch (e: Exception) {
                itemDao.getAllItems().map { it.toDomain() }
            }
        }
    }
}

fun Item.toEntity() = ItemEntity(
    id,
    title = title ?: "",
    description = description ?: "",
    url = url ?: "",
    thumbnailUrl = thumbnailUrl ?: ""
)
fun ItemEntity.toDomain() = Item(id, title, description, url, thumbnailUrl)