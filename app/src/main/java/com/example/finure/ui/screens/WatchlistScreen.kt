package com.finure.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.finure.app.viewmodel.WatchlistViewModel
import com.finure.app.data.model.StockInfo

@Composable
fun WatchlistScreen(viewModel: WatchlistViewModel = hiltViewModel()) {
    val watchlist by viewModel.watchlist.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadWatchlist()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Your Watchlist", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            error != null -> {
                Text(
                    "Error: $error",
                    color = MaterialTheme.colorScheme.error
                )
            }

            watchlist.isEmpty() -> {
                Text(
                    "No stocks added yet.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(watchlist) { stock ->
                        WatchlistCard(stock)
                    }
                }
            }
        }
    }
}

@Composable
fun WatchlistCard(stock: StockInfo) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Text(stock.ticker, style = MaterialTheme.typography.titleMedium)
            Text("Price: \$${stock.price}")
            Text("Change: ${stock.change_percentage}")
        }
    }
}
