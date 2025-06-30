package com.example.finure.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.finure.data.model.StockInfo
import com.example.finure.viewmodel.WatchlistViewModel

/**
 * Displays the details of a specific watchlist.
 * Shows all the stocks inside the selected watchlist in a grid format.
 */
@Composable
fun WatchlistDetailScreen(
    navController: NavHostController,
    name: String,
    viewModel: WatchlistViewModel = hiltViewModel()
) {
    val watchlist by viewModel.watchlist.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Load the watchlist content when the screen appears
    LaunchedEffect(key1 = name, key2 = watchlist) {
        viewModel.selectWatchlist(name)
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Watchlist title
        Text(name, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        when {
            isLoading -> {
                // Show loading indicator
                CircularProgressIndicator()
            }
            watchlist.isEmpty() -> {
                // Show empty state
                Text("No stocks in this watchlist.")
            }
            else -> {
                // Show stocks in a 2-column grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(watchlist) { stock ->
                        WatchlistCard(stock = stock) {
                            navController.navigate("product/${stock.ticker}")
                        }
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(80.dp)) // Padding for safe area / FAB
}

/**
 * Displays a single stock item inside the watchlist grid.
 * Shows ticker, price, and percentage change with dynamic color based on value.
 */
@Composable
fun WatchlistCard(stock: StockInfo, onClick: () -> Unit) {
    val isGainer = !stock.change_percentage.startsWith("-")
    val changeColor = if (isGainer) Color(0xFF2ECC71) else Color(0xFFE74C3C)

    // Fallback for missing change percentage (uses dummy value)
    val percentage = if (stock.change_percentage.isBlank()) {
        val randomValue = 2.32 // Static fallback value
        val formatted = String.format("%.2f", randomValue)
        if (isGainer) "+$formatted%" else "-$formatted%"
    } else {
        stock.change_percentage
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = stock.ticker,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "\$${stock.price}",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 20.sp,
                fontWeight = FontWeight.W500
            )

            Text(
                text = percentage,
                style = MaterialTheme.typography.bodyMedium,
                color = changeColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
