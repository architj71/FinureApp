package com.finure.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.finure.app.navigation.Screen
import com.finure.app.viewmodel.WatchlistViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun WatchlistScreen(
    navController: NavController,
    viewModel: WatchlistViewModel = hiltViewModel()
) {
    val state by viewModel.watchlistState.collectAsState()

    when {
        state.isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.error != null -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${state.error}")
            }
        }

        state.stocks.isEmpty() -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Your watchlist is empty 📭")
            }
        }

        else -> {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                items(state.stocks) { stock ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        onClick = {
                            navController.navigate("${Screen.Product.route}/${stock.symbol}")
                        }
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(stock.symbol, style = MaterialTheme.typography.titleMedium)
                            Text(stock.name, style = MaterialTheme.typography.bodyMedium)
                            Text("Price: $${stock.price}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
