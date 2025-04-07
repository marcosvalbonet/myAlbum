package com.marcosval.myalbum.domain.model

data class Item(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val imageUrl: String
)