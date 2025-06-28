package com.finure.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.finure.app.data.model.StockInfo
import com.finure.app.viewmodel.WatchlistViewModel

@Composable
fun WatchlistListScreen(navController: NavHostController, viewModel: WatchlistViewModel = hiltViewModel()) {
    val names by viewModel.watchlistNames.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadWatchlistNames()
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Watchlist", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        if (names.isEmpty()) {
            Text("No watchlists found")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(names) { name ->
                    WatchlistItem(name) {
                        navController.navigate("watchlist_detail/$name")
                    }
                }
            }
        }
    }
}

@Composable
fun WatchlistItem(name: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, style = MaterialTheme.typography.titleMedium)
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
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
