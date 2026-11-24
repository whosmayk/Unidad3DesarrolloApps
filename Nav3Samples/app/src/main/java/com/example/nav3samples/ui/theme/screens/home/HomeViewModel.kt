package com.example.nav3samples.ui.theme.screens.home

import androidx.lifecycle.viewModelScope
import com.example.nav3samples.data.Product
import com.example.nav3samples.ui.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import com.example.nav3samples.data.products as productList

data class HomeState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel() : BaseViewModel<HomeState>(HomeState()) {

    init {
        loadProducts()
    }

    private fun loadProducts() {
        updateState { copy(isLoading = true, error = null) }
        
        // Simulate network/database call
        viewModelScope.launch {
            try {
                // In a real app, this would be a repository call
                updateState { 
                    copy(
                        products = productList,
                        isLoading = false
                    ) 
                }
            } catch (e: Exception) {
                updateState { 
                    copy(
                        isLoading = false, 
                        error = "Error loading products: ${e.message}"
                    ) 
                }
            }
        }
    }

    fun refresh() {
        loadProducts()
    }
}
