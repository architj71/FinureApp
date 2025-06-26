package com.finure.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.finure.app.viewmodel.ProductViewModel
import com.finure.app.ui.components.LineChartComponent

@Composable
fun ProductScreen(
    navController: NavHostController,
    symbol: String,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val overview by viewModel.stockOverview.collectAsState()
    val isInWatchlist by viewModel.isInWatchlist.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(symbol) {
        viewModel.loadOverview(symbol)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        if (isLoading) {
            CircularProgressIndicator()
        } else if (error != null) {
            Text("Error: $error", color = MaterialTheme.colorScheme.error)
        } else {
            overview?.let { data ->
                Text(data.Name ?: symbol, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(data.Description ?: "", maxLines = 5)

                Spacer(modifier = Modifier.height(16.dp))
                LineChartComponent()

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Add to Watchlist?")
                    Switch(checked = isInWatchlist, onCheckedChange = {
                        viewModel.toggleWatchlist("My Watchlist")

                    })
                }
            } ?: Text("No data available.")
        }
    }
}
