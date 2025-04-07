package com.marcosval.myalbum.domain.repository

import com.marcosval.myalbum.domain.model.Item

interface ItemRepository {
    suspend fun getItems(): List<Item>
}