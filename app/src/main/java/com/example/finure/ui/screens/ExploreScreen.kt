package com.finure.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.finure.app.ui.components.StockCard
import com.example.finure.viewmodel.ExploreViewModel

/**
 * ExploreScreen displays a grid of top gainer stocks.
 *
 * This screen pulls data from the ExploreViewModel and displays it in a 2-column grid layout.
 * It uses a reusable StockCard composable for individual stock items.
 *
 * @param navController Used for screen navigation (currently unused here, but can be extended).
 */
@Composable
fun ExploreScreen(navController: NavHostController) {
    val viewModel: ExploreViewModel = hiltViewModel()
    val gainers by viewModel.topGainers.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Section heading
        Text("Top Gainers", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        // UI logic for empty or filled data
        if (gainers.isEmpty()) {
            Text("No data available", color = MaterialTheme.colorScheme.error)
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxHeight(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(gainers) { stock ->
                    StockCard(stock) {
                        // Optional: Handle navigation or click actions here
                        // e.g., navController.navigate("product/${stock.symbol}")
                    }
                }
            }
        }
    }
}
