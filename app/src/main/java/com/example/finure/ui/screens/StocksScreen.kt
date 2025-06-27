package com.finure.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.finure.app.viewmodel.StockViewModel
import com.finure.app.data.model.StockInfo

@Composable
fun StocksScreen(navController: NavHostController) {
    val viewModel: StockViewModel = hiltViewModel()

    val topMovers by viewModel.gainerData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTopMovers()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // --- Gainers Section ---
        SectionHeader("Top Gainers") {
            navController.navigate("view_all/gainers")
        }
        StockGridSection(topMovers?.top_gainers, isLoading)

        Spacer(modifier = Modifier.height(16.dp))

        // --- Losers Section ---
        SectionHeader("Top Losers") {
            navController.navigate("view_all/losers")
        }
        StockGridSection(topMovers?.top_losers, isLoading)

        error?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun SectionHeader(title: String, onViewAllClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        TextButton(onClick = onViewAllClick) {
            Text("View All")
        }
    }
}

@Composable
fun StockGridSection(stocks: List<StockInfo>?, isLoading: Boolean) {
    if (isLoading) {
        CircularProgressIndicator()
    } else if (stocks.isNullOrEmpty()) {
        Text("No data available.")
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxHeight(0.4f)
                .padding(vertical = 8.dp)
        ) {
            items(stocks) { stock ->
                StockCard(stock)
            }
        }
    }
}

@Composable
fun StockCard(stock: StockInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(8.dp)) {
            Text(stock.ticker, style = MaterialTheme.typography.titleMedium)
            Text("Price: \$${stock.price}")
            Text("Change: ${stock.change_percentage}")
        }
    }
}
