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
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val watchlistNames by viewModel.watchlistNames.collectAsState()
    val selectedWatchlists by viewModel.selectedWatchlists.collectAsState()

    var newWatchlistName by remember { mutableStateOf("") }

    LaunchedEffect(symbol) {
        viewModel.loadOverview(symbol)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else if (error != null) {
            Text("Error: $error", color = MaterialTheme.colorScheme.error)
        } else {
            overview?.let { data ->
                Text("Details Screen", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text("${data.Name} (${data.Symbol})", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))
                LineChartComponent()

                Spacer(modifier = Modifier.height(20.dp))
                Text("Add to Watchlist", style = MaterialTheme.typography.titleMedium)

                // Create New Watchlist
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    OutlinedTextField(
                        value = newWatchlistName,
                        onValueChange = { newWatchlistName = it },
                        label = { Text("New Watchlist Name") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        viewModel.createWatchlist(newWatchlistName)
                        newWatchlistName = ""
                    }) {
                        Text("Add")
                    }
                }

                // Watchlist Checkboxes
                watchlistNames.forEach { name ->
                    val isChecked = name in selectedWatchlists
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { viewModel.toggleWatchlist(name, it) }
                        )
                        Text(name)
                    }
                }
            } ?: Text("No stock data.")
        }
    }
}
