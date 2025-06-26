package com.finure.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finure.app.data.model.StockItem
import com.finure.app.data.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {

    private val _topGainers = MutableStateFlow<List<StockItem>>(emptyList())
    val topGainers: StateFlow<List<StockItem>> = _topGainers

    init {
        fetchGainers()
    }

    private fun fetchGainers() {
        viewModelScope.launch {
            try {
                val response = repository.fetchTopGainers()
                _topGainers.value = response.topGainers
            } catch (e: Exception) {
                // Handle error state
            }
        }
    }
}
