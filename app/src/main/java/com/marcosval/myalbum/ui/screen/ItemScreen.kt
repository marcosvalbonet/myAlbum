package com.marcosval.myalbum.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import com.marcosval.myalbum.ui.viewmodel.ItemUiState
import com.marcosval.myalbum.ui.viewmodel.ItemViewModel
import okhttp3.OkHttpClient

@Composable
fun     ItemScreen(viewModel: ItemViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.fetchItems() }

    when (uiState) {
        is ItemUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(40.dp))
            }
        }
        is ItemUiState.Success -> {
            val items = (uiState as ItemUiState.Success).items
            LazyColumn {
                items(items) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(item.thumbnailUrl, getImageLoader()),
                                contentDescription = item.title,
                                modifier = Modifier.height(50.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = item.title, style = MaterialTheme.typography.titleLarge)
                            }
                        }
                    }
                }
            }
        }
        is ItemUiState.Error -> {
            val errorMessage = (uiState as ItemUiState.Error).message
            Text("Error: $errorMessage")
        }
    }
}

@Composable
fun getImageLoader() = ImageLoader.Builder(LocalContext.current)
        .okHttpClient {
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                        .build()
                    chain.proceed(request)
                }
                .build()
        }
    .respectCacheHeaders(false) // Opcional: para forzar el uso de la caché
    .diskCachePolicy(CachePolicy.ENABLED) // Opcional: para habilitar la caché en disco
    .build()

