package com.marcosval.myalbum.data.remote

import com.marcosval.myalbum.domain.model.Item
import retrofit2.http.GET

interface ApiService {
    @GET("img/shared/technical-test.json")
    suspend fun getItems(): List<Item>
}