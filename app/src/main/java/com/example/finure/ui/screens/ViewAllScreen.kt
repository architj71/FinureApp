package com.example.finure.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.finure.viewmodel.ViewAllViewModel

/**
 * ViewAllScreen displays a scrollable list of stocks based on the selected type (e.g., gainers, losers).
 *
 * @param navController Navigation controller used for routing to the stock detail screen.
 * @param type Type of stocks to display, such as "gainers", "losers", etc.
 */
@Composable
fun ViewAllScreen(navController: NavHostController, type: String) {
    // Inject ViewModel via Hilt
    val viewModel: ViewAllViewModel = hiltViewModel()

    // Observing UI state from ViewModel
    val stocks by viewModel.stocks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Load the stock list when the screen is first launched or when `type` changes
    LaunchedEffect(type) {
        viewModel.loadStocks(type)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Screen title showing selected stock type with capitalized first letter
        Text(
            text = "Top ${type.replaceFirstChar { it.uppercase() }}",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Handle UI states: Loading, Error, Empty, or Data List
        when {
            isLoading -> CircularProgressIndicator() // Loading state
            error != null -> Text("Error: $error", color = MaterialTheme.colorScheme.error) // Error state
            stocks.isEmpty() -> Text("No data available.") // Empty state
            else -> {
                // List of stocks in a LazyColumn
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(stocks) { stock ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Navigate to the product detail screen for the selected stock
                                    navController.navigate("product/${stock.ticker}")
                                }
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Text(stock.ticker, style = MaterialTheme.typography.titleMedium)
                                Text("Price: \$${stock.price}")
                                Text("Change: ${stock.change_percentage}")
                            }
                        }
                    }
                }
            }
        }

        // Extra spacing to avoid bottom bar collision
        Spacer(modifier = Modifier.height(80.dp))
    }
}
