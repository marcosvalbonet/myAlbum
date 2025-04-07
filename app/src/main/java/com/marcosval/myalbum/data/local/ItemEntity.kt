package com.marcosval.myalbum.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String = "",
    val price: Double,
    val imageUrl: String
)