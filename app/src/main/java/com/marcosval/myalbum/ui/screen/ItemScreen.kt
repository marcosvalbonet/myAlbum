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
import com.marcosval.myalbum.ui.viewmodel.ItemUiState
import com.marcosval.myalbum.ui.viewmodel.ItemViewModel
import okhttp3.OkHttpClient

@Composable
fun ItemScreen(viewModel: ItemViewModel = hiltViewModel()) {
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
                        Column(Modifier.padding(16.dp)) {
                            Image(
                                painter = rememberAsyncImagePainter(item.imageUrl, getImageLoader()),
                                contentDescription = item.title,
                                modifier = Modifier.height(150.dp)
                            )
                            Text(text = item.title, style = MaterialTheme.typography.titleLarge)
                            Text(text = "$${item.price}", style = MaterialTheme.typography.titleMedium)
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
                        .header("User-Agent", "MyCustomUserAgent/1.0")
                        .build()
                    chain.proceed(request)
                }
                .build()
        }
        .build()

