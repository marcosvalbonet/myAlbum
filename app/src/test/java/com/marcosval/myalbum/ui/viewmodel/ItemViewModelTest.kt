package com.marcosval.myalbum.ui.viewmodel

import com.marcosval.myalbum.domain.model.Item
import com.marcosval.myalbum.domain.usecase.GetItemsUseCase
import io.mockk.mockk
import io.mockk.coEvery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class ItemViewModelTest {

    private lateinit var viewModel: ItemViewModel
    private lateinit var getItemsUseCase : GetItemsUseCase

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)

        getItemsUseCase = mockk<GetItemsUseCase>()
        viewModel = ItemViewModel(getItemsUseCase)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun shutdown(){
        Dispatchers.resetMain()
    }


    @Test
    fun `fetch items successfully updates UI state`() = runTest {
        val mockItems = listOf(Item(1, "Item 1", "Desc", 10.0, "url"))

        coEvery { getItemsUseCase.invoke() } returns mockItems

        viewModel.fetchItems()

        val state = viewModel.uiState.value
        assertTrue(state is ItemUiState.Success)
        assertEquals(mockItems, (state as ItemUiState.Success).items)
    }

    @Test
    fun `fetch items with error updates UI state to Error`() = runTest {
        coEvery { getItemsUseCase.invoke() }.throws(RuntimeException("API Error"))

        viewModel.fetchItems()

        val state = viewModel.uiState.value
        assertTrue(state is ItemUiState.Error)
        assertEquals("API Error", (state as ItemUiState.Error).message)
    }
}
