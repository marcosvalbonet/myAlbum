package com.marcosval.myalbum.domain.usecase

import com.marcosval.myalbum.domain.model.Item
import com.marcosval.myalbum.domain.repository.ItemRepository
import javax.inject.Inject

class GetItemsUseCase @Inject constructor(private val repository: ItemRepository) {
    suspend operator fun invoke(): List<Item> = repository.getItems()
}