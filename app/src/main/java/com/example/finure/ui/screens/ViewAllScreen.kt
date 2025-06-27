package com.example.finure.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.finure.viewmodel.ViewAllViewModel

@Composable
fun ViewAllScreen(navController: NavHostController, type: String) {
    val viewModel: ViewAllViewModel = hiltViewModel()
    val stocks by viewModel.stocks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(type) {
        viewModel.loadStocks(type)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Top ${type.replaceFirstChar { it.uppercase() }}",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        when {
            isLoading -> CircularProgressIndicator()
            error != null -> Text("Error: $error", color = MaterialTheme.colorScheme.error)
            stocks.isEmpty() -> Text("No data available.")
            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(stocks) { stock ->
                        Card(
                            modifier = Modifier.fillMaxWidth().clickable {
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
    }
}
